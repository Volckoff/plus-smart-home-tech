package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {

    UUID paymentId;

    Double totalPayment;

    Double deliveryTotal;

    Double feeTotal;
}