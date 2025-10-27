package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUserNameAndIsActiveTrue(String userName);

    void deleteByUserName(String userName);
}
