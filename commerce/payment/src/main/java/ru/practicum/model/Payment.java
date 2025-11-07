package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "order_id")
    UUID orderId;

    @Enumerated(value = EnumType.STRING)
    PaymentState state;

    @Column(name = "total_cost")
    Double totalPayment;

    @Column(name = "delivery_cost")
    Double deliveryTotal;

    @Column(name = "fee_cost")
    Double feeTotal;
}
