package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ProductDto;
import ru.practicum.model.Product;

@Component
public class ProductMapper {

    public Product map(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .imageSrc(dto.getImageSrc())
                .quantityState(dto.getQuantityState())
                .productState(dto.getProductState())
                .productCategory(dto.getProductCategory())
                .price(dto.getPrice())
                .build();
    }

    public ProductDto map(Product entity) {
        if (entity == null) {
            return null;
        }

        return new ProductDto(
                entity.getProductId(),
                entity.getProductName(),
                entity.getDescription(),
                entity.getImageSrc(),
                entity.getQuantityState(),
                entity.getProductState(),
                entity.getProductCategory(),
                entity.getPrice()
        );
    }

    public void update(ProductDto dto, Product entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setProductName(dto.getProductName());
        entity.setDescription(dto.getDescription());
        entity.setImageSrc(dto.getImageSrc());
        entity.setQuantityState(dto.getQuantityState());
        entity.setProductState(dto.getProductState());
        entity.setProductCategory(dto.getProductCategory());
        entity.setPrice(dto.getPrice());
    }
}
