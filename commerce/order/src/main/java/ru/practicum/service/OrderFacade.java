package ru.practicum.service;

import ru.practicum.dto.CreateNewOrderRequest;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderFacade {

    List<OrderDto> getClientOrders(String userName);

    OrderDto createNewOrder(CreateNewOrderRequest request);

    OrderDto returnProducts(ProductReturnRequest request);

    OrderDto payOrder(UUID orderId);

    OrderDto successPayOrder(UUID orderId);

    OrderDto failPayOrder(UUID orderId);

    OrderDto deliverOrder(UUID orderId);

    OrderDto failDeliverOrder(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto failAssemblyOrder(UUID orderId);

    OrderDto getOrderById(UUID orderId);
}

