package ru.practicum.clients.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateNewOrderRequest;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequest;
import ru.practicum.valid.Validation;

import java.util.List;
import java.util.UUID;

public interface OrderOperation {

    @GetMapping
    List<OrderDto> getClientOrders(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String username);

    @PutMapping
    OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnProducts(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto failPayOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/success")
    OrderDto successPayOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto failDeliverOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalPrice(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryPrice(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto failAssemblyOrder(@RequestBody @NotNull UUID orderId);
}
