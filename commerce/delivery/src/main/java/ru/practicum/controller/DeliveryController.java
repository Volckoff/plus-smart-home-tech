package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.delivery.DeliveryClient;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    public DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliverySuccessful(orderId);
    }

    @Override
    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliveryPicked(orderId);
    }

    @Override
    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody @NotNull UUID orderId) {
        deliveryService.deliveryFailed(orderId);
    }

    @Override
    @PostMapping("/cost")
    public BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto) {
        return deliveryService.deliveryCost(orderDto);
    }
}






