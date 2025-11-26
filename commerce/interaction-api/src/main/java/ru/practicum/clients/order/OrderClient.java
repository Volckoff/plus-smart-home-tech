package ru.practicum.clients.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order")
public interface OrderClient extends OrderOperation{
}
