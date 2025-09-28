package ru.practicum.sevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.kafka.KafkaConfigProperties;
import ru.practicum.kafka.KafkaEventProducer;
import ru.practicum.mappers.proto.ProtoToAvroSensorMapper;
import ru.practicum.mappers.proto.ProtoToAvroHubMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProtoEventService {

    private final KafkaConfigProperties kafkaProperties;
    private final KafkaEventProducer kafkaEventProducer;

    public void publishSensorEvent(SensorEventProto proto) {
        String topic = kafkaProperties.getSensorEventsTopic();
        String key = proto.getHubId();
        SensorEventAvro avro = ProtoToAvroSensorMapper.toAvro(proto);
        log.info("Publish sensor event | topic={} | key={} | ts={}", topic, key, avro.getTimestamp());
        kafkaEventProducer.send(topic, key, avro);
    }

    public void publishHubEvent(HubEventProto proto) {
        String topic = kafkaProperties.getHubEventsTopic();
        String key = proto.getHubId();
        HubEventAvro avro = ProtoToAvroHubMapper.toAvro(proto);
        log.info("Publish hub event | topic={} | key={} | ts={}", topic, key, avro.getTimestamp());
        kafkaEventProducer.send(topic, key, avro);
    }
}