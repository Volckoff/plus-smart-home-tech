package ru.practicum.clients.delivery;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "delivery")
public interface DeliveryClient extends DeliveryOperation {
}
