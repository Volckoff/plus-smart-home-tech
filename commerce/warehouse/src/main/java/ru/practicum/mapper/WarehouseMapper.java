package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.NewProductInWarehouseRequest;
import ru.practicum.model.WarehouseProduct;

@Component
public class WarehouseMapper {

    public WarehouseProduct map(NewProductInWarehouseRequest dto) {
        if (dto == null) {
            return null;
        }

        return WarehouseProduct.builder()
                .warehouseItemId(dto.getProductId())
                .width(dto.getDimension().getWidth())
                .height(dto.getDimension().getHeight())
                .depth(dto.getDimension().getDepth())
                .quantity(0L)
                .weight(dto.getWeight())
                .fragile(dto.isFragile())
                .build();
    }
}