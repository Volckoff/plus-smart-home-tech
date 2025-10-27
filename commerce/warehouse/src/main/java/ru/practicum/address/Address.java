package ru.practicum.address;

import java.security.SecureRandom;
import java.util.Random;

public class Address {
    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    public static String getAddress() {
        return CURRENT_ADDRESS;
    }
}
