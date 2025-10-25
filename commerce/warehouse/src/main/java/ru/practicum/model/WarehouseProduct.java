package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "warehouse_items")
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseProduct {

    @Id
    @Column(name = "warehouse_item_id")
    UUID warehouseItemId;

    @Column(nullable = false)
    boolean fragile;

    @Column(nullable = false)
    Double width;

    @Column(nullable = false)
    Double height;

    @Column(nullable = false)
    Double depth;

    @Column(nullable = false)
    Double weight;

    @Column(nullable = false)
    long quantity = 0L;
}
