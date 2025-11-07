package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.order.OrderOperation;
import ru.practicum.dto.CreateNewOrderRequest;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequest;
import ru.practicum.service.OrderFacade;
import ru.practicum.valid.Validation;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderOperation {

    private final OrderFacade orderFacade;

    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String username) {
        return orderFacade.getClientOrders(username);
    }

    @Override
    @PutMapping
    public OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        return orderFacade.createNewOrder(request);
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnProducts(@RequestBody @Valid ProductReturnRequest request) {
        return orderFacade.returnProducts(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.payOrder(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto failPayOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.failPayOrder(orderId);
    }

    @Override
    @PostMapping("/payment/success")
    public OrderDto successPayOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.successPayOrder(orderId);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.deliverOrder(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto failDeliverOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.failDeliverOrder(orderId);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.completeOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPrice(@RequestBody @NotNull UUID orderId) {
        return orderFacade.calculateTotalPrice(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryPrice(@RequestBody @NotNull UUID orderId) {
        return orderFacade.calculateDeliveryPrice(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.assemblyOrder(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto failAssemblyOrder(@RequestBody @NotNull UUID orderId) {
        return orderFacade.failAssemblyOrder(orderId);
    }
}

