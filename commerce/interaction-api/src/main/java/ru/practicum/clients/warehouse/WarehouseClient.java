package ru.practicum.clients.warehouse;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient extends WarehouseOperation {
}
