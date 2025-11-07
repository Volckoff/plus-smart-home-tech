package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.practicum.clients.order.OrderClient;
import ru.practicum.clients.warehouse.WarehouseClient;

@SpringBootApplication
@EnableFeignClients(clients = {
        OrderClient.class,
        WarehouseClient.class
})
public class DeliveryApp {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApp.class, args);
    }
}
