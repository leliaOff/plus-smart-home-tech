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
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private final EnumMap<Config.TopicType, String> topics;

    public void start() {
        final String telemetrySensors = topics.get(Config.TopicType.TELEMETRY_SENSORS);
        final String telemetrySnapshots = topics.get(Config.TopicType.TELEMETRY_SNAPSHOTS);
        try {
            consumer.subscribe(Collections.singletonList(telemetrySensors));
            log.info("Слушаем: {}", telemetrySensors);

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();

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

                try {
                    consumer.commitSync();
                } catch (Exception e) {
                    log.error("commitSync error ", e);
                }
            }

        } catch (WakeupException ignored) {
            log.error("Ошибка WakeupException");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(Instant.ofEpochSecond(System.currentTimeMillis()))
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
