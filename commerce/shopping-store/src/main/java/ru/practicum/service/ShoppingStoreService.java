package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.SetProductQuantityStateRequest;

import java.util.UUID;

public interface ShoppingStoreService {

    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto getProductById(UUID productId);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean setQuantityState(SetProductQuantityStateRequest stateRequest);

    boolean deleteProduct(UUID productId);
}
