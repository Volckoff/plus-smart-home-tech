package ru.practicum.clients.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryOperation {

    @PutMapping
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody @NotNull UUID orderId);

    @PostMapping("/picked")
    void deliveryPicked(@RequestBody @NotNull UUID orderId);

    @PostMapping("/failed")
    void deliveryFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto);
}
