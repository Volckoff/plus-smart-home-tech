package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.clients.cart.ShoppingCartClient;
import ru.practicum.clients.delivery.DeliveryClient;
import ru.practicum.clients.payment.PaymentClient;
import ru.practicum.clients.warehouse.WarehouseClient;
import ru.practicum.dto.*;
import ru.practicum.mapper.OrderMapper;
import ru.practicum.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public List<OrderDto> getClientOrders(String userName) {
        log.info("Get orders for user {}", userName);
        return orderService.getClientOrders(userName)
                .stream()
                .map(orderMapper::map)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        Order order = orderService.createNewOrder(getNewOrderFromRequest(request));
        log.info("New order from request: {}", order);
        
        UUID deliveryId = getNewDeliveryId(order.getId(), request.getDeliveryAddress());
        return orderMapper.map(orderService.setDelivery(order.getId(), deliveryId));
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        warehouseClient.acceptReturn(request.getProducts());
        
        return orderMapper.map(orderService.returnProducts(request.getOrderId()));
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderDto orderDto = orderMapper.map(order);
        
        double productCost = paymentClient.getProductCost(orderDto);
        double deliveryCost = deliveryClient.deliveryCost(orderDto);
        
        order.setDeliveryPrice(BigDecimal.valueOf(deliveryCost));
        order.setProductPrice(BigDecimal.valueOf(productCost));
        
        log.info("Order after setting productPrice: {}", order);
        
        double totalCost = paymentClient.getTotalCost(orderMapper.map(order));
        order.setTotalPrice(BigDecimal.valueOf(totalCost));
        
        PaymentDto paymentDto = paymentClient.createPayment(orderMapper.map(order));
        order.setPaymentId(paymentDto.getPaymentId());
        
        Order savedOrder = orderService.savePaymentInfo(order);
        log.info("PayOrder: order after creating payment {}", savedOrder);
        
        return orderMapper.map(savedOrder);
    }

    @Override
    public OrderDto successPayOrder(UUID orderId) {
        return orderMapper.map(orderService.successPayOrder(orderId));
    }

    @Override
    public OrderDto failPayOrder(UUID orderId) {
        return orderMapper.map(orderService.failPayOrder(orderId));
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        return orderMapper.map(orderService.deliverOrder(orderId));
    }

    @Override
    public OrderDto failDeliverOrder(UUID orderId) {
        return orderMapper.map(orderService.failDeliverOrder(orderId));
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return orderMapper.map(orderService.completeOrder(orderId));
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        double totalCost = paymentClient.getTotalCost(orderMapper.map(order));
        return orderMapper.map(orderService.setTotalPrice(orderId, totalCost));
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        double deliveryCost = deliveryClient.deliveryCost(orderMapper.map(order));
        return orderMapper.map(orderService.setDeliveryPrice(orderId, deliveryCost));
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        warehouseClient.assemblyProductsForOrder(getNewAssemblyProductsForOrderRequest(orderId));
        
        return orderMapper.map(orderService.assemblyOrder(orderId));
    }

    @Override
    public OrderDto failAssemblyOrder(UUID orderId) {
        return orderMapper.map(orderService.failAssemblyOrder(orderId));
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        return orderMapper.map(orderService.getOrderById(orderId));
    }

    private Order getNewOrderFromRequest(CreateNewOrderRequest request) {
        BookedProductsDto bookedProductsDto = shoppingCartClient.bookingProductsFromShoppingCart(request.getUserName());
        
        return Order.builder()
                .username(request.getUserName())
                .cartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.getFragile())
                .orderStatus(OrderStatus.NEW)
                .build();
    }

    private UUID getNewDeliveryId(UUID orderId, AddressDto deliveryAddress) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setFromAddress(warehouseClient.getWarehouseAddress());
        deliveryDto.setToAddress(deliveryAddress);
        deliveryDto.setOrderId(orderId);
        deliveryDto.setDeliveryState(DeliveryState.CREATED);
        log.info("New DeliveryDto: {}", deliveryDto);
        return deliveryClient.planDelivery(deliveryDto).getDeliveryId();
    }
    
    private AssemblyProductsForOrderRequest getNewAssemblyProductsForOrderRequest(UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setOrderId(orderId);
        request.setProducts(order.getProducts());
        return request;
    }
}

