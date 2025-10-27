package ru.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.store.StoreOperation;
import ru.practicum.dto.*;
import ru.practicum.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements StoreOperation {
    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam(name = "category") @NotNull ProductCategory category, 
                                      @PageableDefault(size = 10) Pageable pageable) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @Override
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }

    @Override
    public ProductDto createNewProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.addProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        return shoppingStoreService.deleteProduct(productId);
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        return shoppingStoreService.setQuantityState(request);
    }
}
