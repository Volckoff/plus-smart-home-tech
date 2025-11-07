package ru.practicum.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment")
public interface PaymentClient extends PaymentOperation{
}
