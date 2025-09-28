package ru.practicum.mappers.proto;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.stream.Collectors;

@UtilityClass
public class ProtoToAvroHubMapper {

    public HubEventAvro toAvro(HubEventProto proto) {
        return HubEventAvro.newBuilder()
                .setHubId(proto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()
                ))
                .setPayload(toPayloadAvro(proto))
                .build();
    }

    private SpecificRecordBase toPayloadAvro(HubEventProto proto) {
        return switch (proto.getPayloadCase()) {
            case DEVICE_ADDED -> {
                DeviceAddedEventProto p = proto.getDeviceAdded();
                yield DeviceAddedEventAvro.newBuilder()
                        .setId(p.getId())
                        .setType(DeviceTypeAvro.valueOf(p.getType().name()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEventProto p = proto.getDeviceRemoved();
                yield DeviceRemovedEventAvro.newBuilder()
                        .setId(p.getId())
                        .build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEventProto p = proto.getScenarioAdded();
                yield ScenarioAddedEventAvro.newBuilder()
                        .setName(p.getName())
                        .setConditions(p.getConditionList().stream()
                                .map(ProtoToAvroHubMapper::mapCondition)
                                .collect(Collectors.toList()))
                        .setActions(p.getActionList().stream()
                                .map(ProtoToAvroHubMapper::mapAction)
                                .collect(Collectors.toList()))
                        .build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEventProto p = proto.getScenarioRemoved();
                yield ScenarioRemovedEventAvro.newBuilder()
                        .setName(p.getName())
                        .build();
            }
            case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Hub payload is empty");
            default -> throw new IllegalStateException("Unknown hub payload: " + proto.getPayloadCase());
        };
    }

    private ScenarioConditionAvro mapCondition(ScenarioConditionProto c) {
        ScenarioConditionAvro.Builder b = ScenarioConditionAvro.newBuilder()
                .setSensorId(c.getSensorId())
                .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()));

        switch (c.getValueCase()) {
            case BOOL_VALUE -> b.setValue(Boolean.valueOf(c.getBoolValue()));
            case INT_VALUE -> b.setValue(Integer.valueOf(c.getIntValue()));
            case VALUE_NOT_SET -> b.setValue(null);
            default -> throw new IllegalStateException("Unknown condition value: " + c.getValueCase());
        }
        return b.build();
    }

    private DeviceActionAvro mapAction(DeviceActionProto a) {
        DeviceActionAvro.Builder b = DeviceActionAvro.newBuilder()
                .setSensorId(a.getSensorId())
                .setType(ActionTypeAvro.valueOf(a.getType().name()));
        if (a.hasValue()) {
            b.setValue(Integer.valueOf(a.getValue()));
        }
        return b.build();
    }
}