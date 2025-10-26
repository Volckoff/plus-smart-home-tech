package ru.practicum.clients.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequest;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.valid.Validation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartOperation {

    @GetMapping
    ShoppingCartDto getShoppingCart(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName);

    @PutMapping
    ShoppingCartDto addProductsToShoppingCart(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName,
            @RequestBody Map<UUID, @NotNull Long> products);

    @DeleteMapping
    void deleteShoppingCart(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName);

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName,
            @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName,
            @RequestBody @Valid ChangeProductQuantityRequest request);

    @PostMapping("/booking")
    BookedProductsDto bookingProductsFromShoppingCart(
            @RequestParam(name = "username")
            @NotBlank(message = Validation.VALIDATION_USERNAME_MESSAGE)
            String userName);
}
