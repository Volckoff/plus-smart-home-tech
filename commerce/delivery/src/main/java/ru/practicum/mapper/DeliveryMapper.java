package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.model.Delivery;

@Component
public class DeliveryMapper {

    private final AddressMapper addressMapper;

    public DeliveryMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public DeliveryDto map(Delivery entity) {
        if (entity == null) {
            return null;
        }

        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(entity.getDeliveryId());
        dto.setOrderId(entity.getOrderId());
        dto.setFromAddress(addressMapper.map(entity.getFromAddress()));
        dto.setToAddress(addressMapper.map(entity.getToAddress()));
        dto.setDeliveryState(entity.getDeliveryState());
        return dto;
    }
}

