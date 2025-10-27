package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.model.ShoppingCart;

@Component
public class ShoppingCartMapper {

    public ShoppingCart map(ShoppingCartDto dto, String userName) {
        if (dto == null) {
            return null;
        }

        return ShoppingCart.builder()
                .userName(userName)
                .isActive(true)
                .products(dto.getProducts())
                .build();
    }

    public ShoppingCartDto map(ShoppingCart entity) {
        if (entity == null) {
            return null;
        }

        return new ShoppingCartDto(
                entity.getShoppingCartId(),
                entity.getProducts()
        );
    }
}
