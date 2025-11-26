package ru.practicum.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    @NotNull
    UUID orderId;

    UUID shoppingCartId;

    @NotEmpty
    Map<UUID, Long> products;

    UUID paymentId;

    UUID deliveryId;

    OrderStatus state;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;
}
