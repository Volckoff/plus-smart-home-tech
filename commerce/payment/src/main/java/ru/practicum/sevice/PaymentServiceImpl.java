package ru.practicum.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.clients.order.OrderClient;
import ru.practicum.clients.store.ShoppingStoreClient;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.dto.ProductDto;
import ru.practicum.exceptions.NoPaymentFoundException;
import ru.practicum.exceptions.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.mapper.PaymentMapper;
import ru.practicum.model.Payment;
import ru.practicum.model.PaymentState;
import ru.practicum.repository.PaymentRepository;
import ru.practicum.util.PaymentUtil;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        Payment savedPayment = paymentRepository.save(getNewPayment(order));
        return paymentMapper.map(savedPayment);
    }

    @Override
    public Double getTotalCost(OrderDto order) {
        return calcTotalCost(order);
    }

    @Override
    @Transactional
    public void paymentSuccess(UUID orderId) {
        updatePaymentState(orderId, PaymentState.SUCCESS);
        orderClient.successPayOrder(orderId);
    }

    @Override
    public Double getProductCost(OrderDto order) {
        return calcProductsCost(order);
    }

    @Override
    @Transactional
    public void paymentFailed(UUID orderId) {
        updatePaymentState(orderId, PaymentState.FAILED);
        orderClient.failPayOrder(orderId);
    }

    private Payment updatePaymentState(UUID orderId, PaymentState newState) {
        Payment payment = getPaymentByOrderId(orderId);
        payment.setState(newState);
        return paymentRepository.save(payment);
    }

    private Payment getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("The specified payment was not found")
        );
    }

    private Payment getNewPayment(OrderDto order) {
        double productPrice = order.getProductPrice() != null ? order.getProductPrice().doubleValue() : 0.0;
        double fee = calcFeeCost(productPrice);
        return Payment.builder()
                .orderId(order.getOrderId())
                .state(PaymentState.PENDING)
                .totalPayment(order.getTotalPrice() != null ? order.getTotalPrice().doubleValue() : null)
                .deliveryTotal(order.getDeliveryPrice() != null ? order.getDeliveryPrice().doubleValue() : null)
                .feeTotal(fee)
                .build();
    }

    private double calcTotalCost(OrderDto order) {
        double productCost = calcProductsCost(order);
        double deliveryCost = order.getDeliveryPrice() != null ? order.getDeliveryPrice().doubleValue() : 0.0;
        return productCost + calcFeeCost(productCost) + deliveryCost;
    }

    private double calcProductsCost(OrderDto order) {
        double cost = 0;
        Set<UUID> productIds = order.getProducts().keySet();
        Map<UUID, ProductDto> products = shoppingStoreClient.getProductByIds(productIds)
                .stream()
                .collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));

        for (Map.Entry<UUID, Long> orderProduct : order.getProducts().entrySet()) {
            Long quantity = orderProduct.getValue();
            if (!products.containsKey(orderProduct.getKey())) {
                throw new NotEnoughInfoInOrderToCalculateException("There is not enough information in the order " +
                        "for calculation");
            }
            double price = products.get(orderProduct.getKey()).getPrice();
            cost += price * quantity;
        }

        return cost;
    }

    private double calcFeeCost(double cost) {
        return cost * PaymentUtil.BASE_VAT_RATE;
    }
}
