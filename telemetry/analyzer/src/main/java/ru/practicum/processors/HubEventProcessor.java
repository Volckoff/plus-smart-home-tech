package ru.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.KafkaConfigProperties;
import ru.practicum.services.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> hubEventConsumer;
    private final KafkaConfigProperties kafkaProperties;
    private final List<HubEventHandler> hubEventHandlers;

    @Override
    public void run() {
        log.info("Starting hub event processor...");

        try {
            hubEventConsumer.subscribe(Collections.singletonList(kafkaProperties.getTopics().getHubEvents()));

            while (true) {
                try {
                    ConsumerRecords<String, HubEventAvro> records =
                            hubEventConsumer.poll(Duration.ofMillis(kafkaProperties.getConsumer().getConsumeTimeout()));

                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        log.info("Received hub event: {}", record.key());
                        processHubEvent(record.value());
                    }
                    if (!records.isEmpty()) {
                        hubEventConsumer.commitSync();
                    }
                } catch (Exception e) {
                    log.error("Error processing hub event", e);
                }
            }
        } catch (Exception e) {
            log.error("Error in hub event processor", e);
        }
    }

    private void processHubEvent(HubEventAvro hubEvent) {
        log.info("Processing hub event: {}", hubEvent.getPayload().getClass().getSimpleName());

        try {
            String payloadType = hubEvent.getPayload().getClass().getSimpleName();
            for (HubEventHandler handler : hubEventHandlers) {
                if (handler.getTypeOfPayload().equals(payloadType)) {
                    handler.handle(hubEvent);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error while processing hub event", e);
        }
    }
}