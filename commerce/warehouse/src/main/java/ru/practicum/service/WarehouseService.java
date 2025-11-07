package ru.practicum.service;

import ru.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseRequest request);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void shippedToDelivery(ShippedToDeliveryRequest request);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);

    void acceptReturn(Map<UUID, Long> products);
}
