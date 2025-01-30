package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.configuration.Config;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private static final Duration CONSUMER_TIMEOUT = Duration.ofMillis(1000);
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final ConcurrentHashMap<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();
    private final EnumMap<Config.TopicType, String> topics;
    private String telemetrySensors;
    private String telemetrySnapshots;

    public void start() {
        if (!setTelemetrySensors() || !setTelemetrySnapshots()) {
            return;
        }
        try {
            consumer.subscribe(Collections.singletonList(telemetrySensors));
            log.info("Слушаем: {}", telemetrySensors);
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUMER_TIMEOUT);
                if (records.isEmpty()) {
                    continue;
                }
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    processing(record.value());
                }
                try {
                    consumer.commitSync();
                } catch (Exception e) {
                    log.error("Ошибка фиксации", e);
                }
            }
        } catch (WakeupException ignored) {
            log.info("Остановка потребителя");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitAsync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private boolean setTelemetrySensors() {
        telemetrySensors = topics.get(Config.TopicType.TELEMETRY_SENSORS);
        if (telemetrySensors == null) {
            log.error("Не удалось получить топик для прослушивания датчиков");
            return false;
        }
        return true;
    }

    private boolean setTelemetrySnapshots() {
        telemetrySnapshots = topics.get(Config.TopicType.TELEMETRY_SNAPSHOTS);
        if (telemetrySnapshots == null) {
            log.error("Не удалось получить топик для фиксации снапшотов");
            return false;
        }
        return true;
    }

    private void processing(SensorEventAvro event) {
        updateState(event).ifPresent(snapshot -> {
            try {
                producer.send(
                        new ProducerRecord<>(telemetrySnapshots, snapshot.getHubId(), snapshot),
                        (metadata, exception) -> {
                        });
                log.info("Зафиксирован снапшоп {}", snapshot.getHubId());
            } catch (Exception e) {
                log.error("Ошибка при отправке снапшота в топик", e);
            }
        });
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(Instant.now())
                        .setSensorsState(new HashMap<>())
                        .build());

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null
                && !oldState.getTimestamp().isBefore(event.getTimestamp())
                && oldState.getData().equals(event.getPayload())) {
            return Optional.empty();
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.getSensorsState().put(event.getId(), newState);

        snapshot.setTimestamp(event.getTimestamp());

        snapshots.put(event.getHubId(), snapshot);

        return Optional.of(snapshot);
    }

}
