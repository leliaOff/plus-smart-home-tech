package ru.yandex.practicum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Analyzer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);

        final HubEventProcessor hubEventProcessor = context.getBean(HubEventProcessor.class);
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        final SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);
        snapshotProcessor.run();
    }
}
