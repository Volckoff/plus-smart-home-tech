package ru.practicum.service;

import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void deliverySuccessful(UUID orderId);

    void deliveryPicked(UUID orderId);

    void deliveryFailed(UUID orderId);

    Double deliveryCost(OrderDto orderDto);
}
