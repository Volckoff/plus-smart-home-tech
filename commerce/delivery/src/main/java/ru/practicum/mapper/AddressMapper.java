package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.AddressDto;
import ru.practicum.model.Address;

@Component
public class AddressMapper {

    public Address map(AddressDto dto) {
        if (dto == null) {
            return null;
        }

        return Address.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .street(dto.getStreet())
                .house(dto.getHouse())
                .flat(dto.getFlat())
                .build();
    }

    public AddressDto map(Address entity) {
        if (entity == null) {
            return null;
        }

        AddressDto dto = new AddressDto();
        dto.setCountry(entity.getCountry());
        dto.setCity(entity.getCity());
        dto.setStreet(entity.getStreet());
        dto.setHouse(entity.getHouse());
        dto.setFlat(entity.getFlat());
        return dto;
    }
}

