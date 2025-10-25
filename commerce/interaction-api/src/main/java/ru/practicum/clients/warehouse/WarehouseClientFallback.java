package ru.practicum.clients.warehouse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.*;

@Slf4j
@Component
public class WarehouseClientFallback implements WarehouseOperation {

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        log.warn("Warehouse service is unavailable. Fallback: ignoring newProductInWarehouse request for product: {}", 
                request.getProductId());
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto) {
        log.warn("Warehouse service is unavailable. Fallback: returning default BookedProductsDto for cart: {}", 
                shoppingCartDto.getShoppingCartId());
        
        return new BookedProductsDto(
                0.0,
                0.0,
                false
        );
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        log.warn("Warehouse service is unavailable. Fallback: ignoring addProductToWarehouse request for product: {}", 
                request.getProductId());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.warn("Warehouse service is unavailable. Fallback: returning default address");
        
        return new AddressDto(
                "The warehouse is temporarily unavailable.",
                "Address will be provided later",
                "Contact information is unavailable",
                "Opening hours: to be confirmed",
                "No further information available"
        );
    }
}
