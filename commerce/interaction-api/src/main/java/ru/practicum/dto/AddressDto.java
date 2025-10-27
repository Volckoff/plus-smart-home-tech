package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    String country;

    String city;

    String street;

    String house;

    String flat;
}