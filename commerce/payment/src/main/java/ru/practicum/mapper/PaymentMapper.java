package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.PaymentDto;
import ru.practicum.model.Payment;

@Component
public class PaymentMapper {

    public PaymentDto map(Payment entity) {
        if (entity == null) {
            return null;
        }

        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(entity.getPaymentId());
        dto.setTotalPayment(entity.getTotalPayment());
        dto.setDeliveryTotal(entity.getDeliveryTotal());
        dto.setFeeTotal(entity.getFeeTotal());
        return dto;
    }
}
