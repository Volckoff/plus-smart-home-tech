package ru.practicum.clients.store;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient extends StoreOperation {
}