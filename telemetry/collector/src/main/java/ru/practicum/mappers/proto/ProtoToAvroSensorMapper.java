package ru.practicum.mappers.proto;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@UtilityClass
public class ProtoToAvroSensorMapper {

    public SensorEventAvro toAvro(SensorEventProto proto) {
        return SensorEventAvro.newBuilder()
                .setId(proto.getId())
                .setHubId(proto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()
                ))
                .setPayload(toPayloadAvro(proto))
                .build();
    }

    private SpecificRecordBase toPayloadAvro(SensorEventProto proto) {
        return switch (proto.getPayloadCase()) {
            case MOTION_SENSOR_EVENT -> {
                MotionSensorProto p = proto.getMotionSensorEvent();
                yield MotionSensorAvro.newBuilder()
                        .setLinkQuality(p.getLinkQuality())
                        .setMotion(p.getMotion())
                        .setVoltage(p.getVoltage())
                        .build();
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorProto p = proto.getTemperatureSensorEvent();
                yield TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(p.getTemperatureC())
                        .setTemperatureF(p.getTemperatureF())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorProto p = proto.getLightSensorEvent();
                yield LightSensorAvro.newBuilder()
                        .setLinkQuality(p.getLinkQuality())
                        .setLuminosity(p.getLuminosity())
                        .build();
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorProto p = proto.getClimateSensorEvent();
                yield ClimateSensorAvro.newBuilder()
                        .setTemperatureC(p.getTemperatureC())
                        .setHumidity(p.getHumidity())
                        .setCo2Level(p.getCo2Level())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorProto p = proto.getSwitchSensorEvent();
                yield SwitchSensorAvro.newBuilder()
                        .setState(p.getState())
                        .build();
            }
            case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Sensor payload is empty");
            default -> throw new IllegalStateException("Unknown sensor payload: " + proto.getPayloadCase());
        };
    }
}