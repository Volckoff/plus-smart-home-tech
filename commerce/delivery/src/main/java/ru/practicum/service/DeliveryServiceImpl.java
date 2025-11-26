package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.clients.order.OrderClient;
import ru.practicum.clients.warehouse.WarehouseClient;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.DeliveryState;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ShippedToDeliveryRequest;
import ru.practicum.exceptions.NoDeliveryFoundException;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.DeliveryMapper;
import ru.practicum.model.Address;
import ru.practicum.model.Delivery;
import ru.practicum.repository.DeliveryRepository;
import ru.practicum.util.DeliveryUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryRepository.save(getNewDelivery(deliveryDto));
        return deliveryMapper.map(delivery);
    }

    @Override
    @Transactional
    public void deliverySuccessful(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.deliveryOrder(orderId);
    }

    @Override
    @Transactional
    public void deliveryPicked(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        
        ShippedToDeliveryRequest request = getNewShippedToDeliveryRequest(delivery);
        warehouseClient.shippedToDelivery(request);
        
        orderClient.assemblyOrder(orderId);
    }

    @Override
    @Transactional
    public void deliveryFailed(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.failDeliverOrder(orderId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        UUID orderId = orderDto.getOrderId();
        log.info("Starting delivery cost calculation for orderId: {}", orderId);
        
        Delivery delivery = getDeliveryByOrderId(orderId);
        log.info("Delivery found for orderId {}: {}", orderId, delivery);

        BigDecimal cost = DeliveryUtil.BASE_DELIVERY_PRICE;
        log.info("Base delivery price for orderId {}: {}", orderId, cost);
        
        // Умножаем базовую стоимость на коэффициент адреса склада и складываем с базовой стоимостью
        BigDecimal addressMultiplier = getCoefByFromAddress(delivery.getFromAddress());
        log.info("Address multiplier for orderId {}: {}", orderId, addressMultiplier);
        BigDecimal addressCost = DeliveryUtil.BASE_DELIVERY_PRICE.multiply(addressMultiplier);
        cost = cost.add(addressCost);
        log.info("Cost after address multiplier for orderId {}: {}", orderId, cost);

        // Если хрупкий, умножаем сумму на 0.2 и складываем
        if (orderDto.getFragile() != null && orderDto.getFragile()) {
            BigDecimal fragileMultiplier = BigDecimal.valueOf(0.2);
            BigDecimal fragileCost = cost.multiply(fragileMultiplier);
            cost = cost.add(fragileCost);
            log.info("Fragile item detected for orderId {}, added cost: {}, total cost: {}", 
                    orderId, fragileCost, cost);
        }

        // Добавляем вес, умноженный на 0.3
        if (orderDto.getDeliveryWeight() != null) {
            BigDecimal weightMultiplier = BigDecimal.valueOf(0.3);
            BigDecimal weightCost = BigDecimal.valueOf(orderDto.getDeliveryWeight())
                    .multiply(weightMultiplier);
            cost = cost.add(weightCost);
            log.info("Weight cost for orderId {}: weight={}, cost added: {}, total cost: {}", 
                    orderId, orderDto.getDeliveryWeight(), weightCost, cost);
        }

        // Добавляем объём, умноженный на 0.2
        if (orderDto.getDeliveryVolume() != null) {
            BigDecimal volumeMultiplier = BigDecimal.valueOf(0.2);
            BigDecimal volumeCost = BigDecimal.valueOf(orderDto.getDeliveryVolume())
                    .multiply(volumeMultiplier);
            cost = cost.add(volumeCost);
            log.info("Volume cost for orderId {}: volume={}, cost added: {}, total cost: {}", 
                    orderId, orderDto.getDeliveryVolume(), volumeCost, cost);
        }

        // Если улица доставки не совпадает с улицей склада, умножаем сумму на 0.2 и складываем
        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            BigDecimal streetMultiplier = BigDecimal.valueOf(0.2);
            BigDecimal streetCost = cost.multiply(streetMultiplier);
            cost = cost.add(streetCost);
            log.info("Different street detected for orderId {}, added cost: {}, total cost: {}", 
                    orderId, streetCost, cost);
        }

        BigDecimal finalCost = cost.setScale(2, RoundingMode.HALF_UP);
        log.info("Final delivery cost calculated for orderId {}: {}", orderId, finalCost);
        return finalCost;
    }

    private BigDecimal getCoefByFromAddress(Address address) {
        if (address == null || address.getStreet() == null) {
            return DeliveryUtil.BASE_ADDRESS_COEF;
        }
        
        String street = address.getStreet();
        if (street.contains("ADDRESS_1")) {
            return DeliveryUtil.ADDRESS_1_ADDRESS_COEF;
        } else if (street.contains("ADDRESS_2")) {
            return DeliveryUtil.ADDRESS_2_ADDRESS_COEF;
        } else {
            return DeliveryUtil.BASE_ADDRESS_COEF;
        }
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoDeliveryFoundException("Delivery not found")
        );
    }

    private ShippedToDeliveryRequest getNewShippedToDeliveryRequest(Delivery delivery) {
        return new ShippedToDeliveryRequest(
                delivery.getOrderId(),
                delivery.getDeliveryId()
        );
    }

    private Delivery getNewDelivery(DeliveryDto deliveryDto) {
        log.info("GetNewDelivery for order id = {}", deliveryDto.getOrderId());
        return Delivery.builder()
                .orderId(deliveryDto.getOrderId())
                .fromAddress(addressMapper.map(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.map(deliveryDto.getToAddress()))
                .deliveryState(DeliveryState.CREATED)
                .build();
    }
}