package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.configuration.Config;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.AnalyzerService;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final Config config;
    private final AnalyzerService service;

    public SnapshotProcessor(Config config, AnalyzerService service) {
        this.config = config;
        this.service = service;
        this.consumer = new KafkaConsumer<>(config.getHubConsumerProperties());
    }

    @Override
    public void run() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(Collections.singletonList("telemetry.snapshots.v1"));
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    try {
                        processing(record);
                    } catch (Exception e) {
                        log.error("При обработке сообщения произошла ошибка: {}", record, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ошибка kafka consumer", e);
        }
    }

    @KafkaListener(topics = "telemetry.snapshots.v1", groupId = "analyzer-snapshots-group")
    public void processing(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        service.processing(record.value());
    }
}
