package ru.practicum.sevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.practicum.event.hub.HubEvent;
import ru.practicum.event.sensor.SensorEvent;
import ru.practicum.kafka.KafkaConfigProperties;
import ru.practicum.kafka.KafkaEventProducer;
import ru.practicum.mappers.HubEventMapper;
import ru.practicum.mappers.SensorEventMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final KafkaConfigProperties kafkaProperties;
    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public void publishSensorEvent(SensorEvent event) {
        String topic = kafkaProperties.getSensorEventsTopic();
        String key = event.getHubId();
        SpecificRecordBase avroEvent = SensorEventMapper.toSensorEventAvro(event);

        log.info("Preparing to publish sensor event to Kafka | topic={} | key={} | timestamp={}",
                topic, key, event.getTimestamp().toEpochMilli());

        kafkaEventProducer.send(topic, key, avroEvent);
    }

    @Override
    public void publishHubEvent(HubEvent event) {
        String topic = kafkaProperties.getHubEventsTopic();
        String key = event.getHubId();
        SpecificRecordBase avroEvent = HubEventMapper.toHubEventAvro(event);

        log.info("Preparing to publish hub event to Kafka | topic={} | key={} | timestamp={}",
                topic, key, event.getTimestamp().toEpochMilli());

        kafkaEventProducer.send(topic, key, avroEvent);
    }
}
