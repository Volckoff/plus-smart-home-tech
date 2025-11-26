package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    UUID deliveryId;

    @Column(name = "order_id")
    UUID orderId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    Address fromAddress;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    Address toAddress;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    DeliveryState deliveryState;

    Double deliveryWeight;

    Double deliveryVolume;

    boolean fragile;
}
