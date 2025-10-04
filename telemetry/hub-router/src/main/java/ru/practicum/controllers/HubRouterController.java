package ru.practicum.controllers;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import com.google.protobuf.Empty;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class HubRouterController extends HubRouterControllerGrpc.HubRouterControllerImplBase {

    @Override
    public void handleDeviceAction(DeviceActionRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received device action request:");
        log.info("  Hub ID: {}", request.getHubId());
        log.info("  Scenario: {}", request.getScenarioName());
        log.info("  Device ID: {}", request.getAction().getSensorId());
        log.info("  Device Type: {}", request.getAction().getType());
        log.info("  Value: {}", request.getAction().getValue());
        log.info("  Timestamp: {}", request.getTimestamp());

        log.info("Device action processed successfully");

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}