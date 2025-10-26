package ru.practicum.clients.cart;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient extends CartOperation {
}
