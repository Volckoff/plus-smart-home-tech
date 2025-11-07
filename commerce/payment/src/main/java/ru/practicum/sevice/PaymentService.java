package ru.practicum.sevice;

import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    Double getTotalCost(OrderDto order);

    void paymentSuccess(UUID orderId);

    Double getProductCost(OrderDto order);

    void paymentFailed(UUID orderId);
}
