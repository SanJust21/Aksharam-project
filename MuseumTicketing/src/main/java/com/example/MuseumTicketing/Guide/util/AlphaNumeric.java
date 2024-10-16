package com.example.MuseumTicketing.Guide.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;

@Service
public class AlphaNumeric {
    Integer length = 10;
    public String generateRandomNumber() {
        //int length =10;
        return RandomStringUtils.randomAlphanumeric(length);
    }

    private static final String BOOKING_ID_PREFIX = "AKM_SPOT";
    Integer spotLength=4;
    public String generateSpotRandomNumber(){
        String number = RandomStringUtils.randomNumeric(spotLength);
        return BOOKING_ID_PREFIX+number;
    }
}
