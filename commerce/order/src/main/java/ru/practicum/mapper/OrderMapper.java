package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.OrderDto;
import ru.practicum.model.Order;

@Component
public class OrderMapper {

    public OrderDto map(Order entity) {
        if (entity == null) {
            return null;
        }
        OrderDto dto = new OrderDto();
        dto.setOrderId(entity.getId());
        dto.setShoppingCartId(entity.getCartId());
        dto.setProducts(entity.getProducts());
        dto.setPaymentId(entity.getPaymentId());
        dto.setDeliveryId(entity.getDeliveryId());
        dto.setState(entity.getOrderStatus());
        dto.setDeliveryWeight(entity.getDeliveryWeight());
        dto.setDeliveryVolume(entity.getDeliveryVolume());
        dto.setFragile(entity.getFragile());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setDeliveryPrice(entity.getDeliveryPrice());
        dto.setProductPrice(entity.getProductPrice());

        return dto;
    }
}

