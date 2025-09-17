package ru.practicum.sevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.event.hub.HubEvent;
import ru.practicum.event.sensor.SensorEvent;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.mappers.HubEventMapper;
import ru.practicum.mappers.SensorEventMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final KafkaConfig kafkaConfig;
    private final Producer<String, SpecificRecordBase> producer;

    @Override
    public void publishSensorEvent(SensorEvent event) {
        String topic = kafkaConfig.getKafkaProperties().getSensorEventsTopic();
        String key = event.getHubId();
        long timestamp = event.getTimestamp().toEpochMilli();
        SpecificRecordBase avroEvent = SensorEventMapper.toSensorEventAvro(event);
        log.info("Preparing to publish sensor event to Kafka | topic={} | key={} | timestamp={}",
                topic, key, timestamp);

        send(topic, key, timestamp, avroEvent);
    }

    @Override
    public void publishHubEvent(HubEvent event) {
        String topic = kafkaConfig.getKafkaProperties().getHubEventsTopic();
        String key = event.getHubId();
        long timestamp = event.getTimestamp().toEpochMilli();
        SpecificRecordBase avroEvent = HubEventMapper.toHubEventAvro(event);
        log.info("Preparing to publish hub event to Kafka | topic={} | key={} | timestamp={}",
                topic, key, timestamp);

        send(topic, key, timestamp, avroEvent);
    }

    private void send(String topic, String key, long timestamp, SpecificRecordBase value) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp,
                key,
                value
        );

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to publish event to Kafka | topic={} | key={} | error={}",
                        topic, key, exception.getMessage(), exception);
            } else {
                log.debug("Successfully published event to Kafka | topic={} | partition={} | offset={}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}
