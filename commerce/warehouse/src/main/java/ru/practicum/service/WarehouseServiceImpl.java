package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.address.Address;
import ru.practicum.dto.*;
import ru.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.mapper.WarehouseMapper;
import ru.practicum.model.Booking;
import ru.practicum.model.WarehouseProduct;
import ru.practicum.repository.BookingRepository;
import ru.practicum.repository.WarehouseRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "A product with this description is already registered in the warehouse");
        }
        warehouseRepository.save(warehouseMapper.map(request));
    }

    @Override
    @Transactional
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        WarehouseProduct product = getWarehouseProduct(request.getProductId());
        long newQuantity = product.getQuantity() + request.getQuantity();
        product.setQuantity(newQuantity);
        warehouseRepository.save(product);
    }

    @Override
    @Transactional
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> cartProducts = shoppingCartDto.getProducts();
        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(cartProducts.keySet())
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getWarehouseItemId, Function.identity()));
        if (products.size() != cartProducts.size()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Some products are out of stock");
        }
        double weight = 0;
        double volume = 0;
        boolean fragile = false;
        for (Map.Entry<UUID, Long> cartProduct : cartProducts.entrySet()) {
            WarehouseProduct product = products.get(cartProduct.getKey());
            if (cartProduct.getValue() > product.getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        "The item in the cart is not in the required quantity in the warehouse");
            }
            weight += product.getWeight() * cartProduct.getValue();
            volume += product.getHeight() * product.getWeight() * product.getDepth() * cartProduct.getValue();
            fragile = fragile || product.isFragile();
        }

        return new BookedProductsDto(
                weight,
                volume,
                fragile
        );
    }

    @Override
    public AddressDto getWarehouseAddress() {
        String defValue = Address.getAddress();
        return new AddressDto(
                defValue,
                defValue,
                defValue,
                defValue,
                defValue
        );
    }

    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        Booking booking = getBookingByOrderId(request.getOrderId());
        booking.setDeliveryId(request.getDeliveryID());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> orderProducts = request.getProducts();
        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(orderProducts.keySet())
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getWarehouseItemId, Function.identity()));

        if (products.size() != orderProducts.size()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Some products are out of stock");
        }

        double weight = 0;
        double volume = 0;
        boolean fragile = false;

        // Проверяем наличие товаров и уменьшаем количество
        for (Map.Entry<UUID, Long> orderProduct : orderProducts.entrySet()) {
            WarehouseProduct product = products.get(orderProduct.getKey());
            if (product == null) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        "Product not found in warehouse: " + orderProduct.getKey());
            }
            if (orderProduct.getValue() > product.getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        "The item in the order is not in the required quantity in the warehouse");
            }

            // Уменьшаем количество товара на складе
            long newQuantity = product.getQuantity() - orderProduct.getValue();
            product.setQuantity(newQuantity);
            warehouseRepository.save(product);

            // Рассчитываем вес, объём и хрупкость
            weight += product.getWeight() * orderProduct.getValue();
            volume += product.getHeight() * product.getWeight() * product.getDepth() * orderProduct.getValue();
            fragile = fragile || product.isFragile();
        }

        // Создаём бронирование
        Map<UUID, Integer> bookingProducts = new HashMap<>();
        for (Map.Entry<UUID, Long> orderProduct : orderProducts.entrySet()) {
            bookingProducts.put(orderProduct.getKey(), orderProduct.getValue().intValue());
        }

        Booking booking = Booking.builder()
                .orderId(request.getOrderId())
                .products(bookingProducts)
                .build();
        bookingRepository.save(booking);

        return new BookedProductsDto(weight, volume, fragile);
    }

    @Override
    @Transactional
    public void acceptReturn(Map<UUID, Long> products) {
        for (Map.Entry<UUID, Long> returnProduct : products.entrySet()) {
            WarehouseProduct product = getWarehouseProduct(returnProduct.getKey());
            long newQuantity = product.getQuantity() + returnProduct.getValue();
            product.setQuantity(newQuantity);
            warehouseRepository.save(product);
        }
    }

    private Booking getBookingByOrderId(UUID orderId) {
        return bookingRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException("Booking not found for order: " + orderId)
        );
    }

    private WarehouseProduct getWarehouseProduct(UUID productId) {
        return warehouseRepository.findById(productId).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException("Here is no information about the product in warehouse")
        );
    }
}