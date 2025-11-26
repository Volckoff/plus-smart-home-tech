package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.practicum.clients.order.OrderClient;
import ru.practicum.clients.store.ShoppingStoreClient;

@SpringBootApplication
@EnableFeignClients(clients = {
        ShoppingStoreClient.class,
        OrderClient.class
})
public class PaymentApp {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }
}