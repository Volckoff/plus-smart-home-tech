package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.practicum.clients.cart.ShoppingCartClient;
import ru.practicum.clients.delivery.DeliveryClient;
import ru.practicum.clients.payment.PaymentClient;
import ru.practicum.clients.warehouse.WarehouseClient;

@SpringBootApplication
@EnableFeignClients(clients = {
        ShoppingCartClient.class,
        WarehouseClient.class,
        PaymentClient.class,
        DeliveryClient.class
})
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
