package ru.practicum.kafka;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Getter
@Configuration
@EnableConfigurationProperties({KafkaConfigProperties.class})
public class KafkaConfig {
    private final KafkaConfigProperties kafkaProperties;

    public KafkaConfig(KafkaConfigProperties properties) {
        this.kafkaProperties = properties;
    }

    @Bean
    public Producer<String, SpecificRecordBase> producer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientIdConfig());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducerKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducerValueSerializer());
        return new KafkaProducer<>(props);
    }
}
