package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.configuration.Config;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.ScenarioService;
import ru.yandex.practicum.service.SensorService;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Config config;
    private final SensorService sensorService;
    private final ScenarioService scenarioService;

    public HubEventProcessor(Config config, SensorService sensorService, ScenarioService scenarioService) {
        this.config = config;
        this.sensorService = sensorService;
        this.scenarioService = scenarioService;
        this.consumer = new KafkaConsumer<>(config.getHubConsumerProperties());
    }

    @Override
    public void run() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(Collections.singletonList("telemetry.hubs.v1"));
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
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

    @KafkaListener(topics = "telemetry.hubs.v1", groupId = "analyzer-hub-group")
    public void processing(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro hubEventAvro = record.value();
        if (hubEventAvro.getPayload() instanceof DeviceAddedEventAvro event) {
            sensorService.addSensor(event.getId(), hubEventAvro.getHubId());
            return;
        }
        if (hubEventAvro.getPayload() instanceof DeviceRemovedEventAvro event) {
            sensorService.removeSensor(event.getId(), hubEventAvro.getHubId());
            return;
        }
        if (hubEventAvro.getPayload() instanceof ScenarioAddedEventAvro event) {
            scenarioService.addScenario(event, hubEventAvro.getHubId());
            return;
        }
        if (hubEventAvro.getPayload() instanceof ScenarioRemovedEventAvro event) {
            scenarioService.removeScenario(event.getName());
            return;
        }
        log.warn("Не удалось определить тип события: {}", hubEventAvro.getPayload().getClass().getName());
    }
}
