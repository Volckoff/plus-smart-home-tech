package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.payment.PaymentOperation;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.sevice.PaymentService;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentOperation {

    private final PaymentService paymentService;

    @Override
    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid OrderDto order) {
        return paymentService.createPayment(order);
    }

    @Override
    @PostMapping("/totalCost")
    public Double getTotalCost(@RequestBody @Valid OrderDto order) {
        return paymentService.getTotalCost(order);
    }

    @Override
    @PostMapping("/refund")
    public void paymentSuccess(@RequestBody @NotNull UUID orderId) {
        paymentService.paymentSuccess(orderId);
    }

    @Override
    @PostMapping("/productCost")
    public Double getProductCost(@RequestBody @Valid OrderDto order) {
        return paymentService.getProductCost(order);
    }

    @Override
    @PostMapping("/failed")
    public void paymentFailed(@RequestBody @NotNull UUID orderId) {
        paymentService.paymentFailed(orderId);
    }
}




