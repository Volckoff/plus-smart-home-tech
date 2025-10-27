package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.clients.warehouse.WarehouseClient;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequest;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.exceptions.NoProductsInShoppingCartException;
import ru.practicum.exceptions.NotAuthorizedUserException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.ShoppingCartMapper;
import ru.practicum.model.ShoppingCart;
import ru.practicum.repository.ShoppingCartRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String userName) {
        return shoppingCartMapper.map(getShoppingCartByUsername(userName));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductsToShoppingCart(String userName, Map<UUID, Long> products) {
        ShoppingCart cart = shoppingCartRepository.findByUserNameAndIsActiveTrue(userName).orElse(
                getNewShoppingCart(userName)
        );
        Map<UUID, Long> cartProducts = new HashMap<>();
        if (cart.getProducts() != null && !cart.getProducts().isEmpty()) {
            cartProducts.putAll(cart.getProducts());
        }
        cartProducts.putAll(products);
        cart.setProducts(cartProducts);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return shoppingCartMapper.map(savedCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String userName, List<UUID> products) {
        ShoppingCart cart = getShoppingCartByUsername(userName);
        products.forEach(productId -> cart.getProducts().remove(productId));
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return shoppingCartMapper.map(savedCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest request) {
        ShoppingCart cart = getShoppingCartByUsername(userName);
        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException("There are no products you are looking for in your cart.");
        }
        cart.getProducts().put(request.getProductId(), request.getNewQuantity());
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return shoppingCartMapper.map(savedCart);
    }

    @Override
    @Transactional
    public BookedProductsDto bookingProductsFromShoppingCart(String userName) {
        ShoppingCartDto cart = shoppingCartMapper.map(getShoppingCartByUsername(userName));
        BookedProductsDto result = warehouseClient.checkProductQuantityEnoughForShoppingCart(cart);
        log.info("Booking result for user {}: weight={}, volume={}, fragile={}",
                userName, result.getDeliveryWeight(), result.getDeliveryVolume(), result.getFragile());
        
        return result;
    }

    @Override
    @Transactional
    public void deleteShoppingCart(String userName) {
        ShoppingCart cart = getShoppingCartByUsername(userName);
        cart.setIsActive(false);
        shoppingCartRepository.save(cart);
    }

    private ShoppingCart getShoppingCartByUsername(String userName) {
        return shoppingCartRepository.findByUserNameAndIsActiveTrue(userName).orElseThrow(
                () -> new NotFoundException("Shopping cart not found")
        );
    }

    private ShoppingCart getNewShoppingCart(String userName) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserName(userName);
        cart.setIsActive(true);
        return cart;
    }
}
