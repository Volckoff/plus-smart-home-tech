package ru.practicum.clients.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentOperation {

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody @Valid OrderDto order);

    @PostMapping("/refund")
    void paymentSuccess(@RequestBody @NotNull UUID orderId);

    @PostMapping("/productCost")
    Double getProductCost(@RequestBody @Valid OrderDto order);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody @NotNull UUID orderId);
}
