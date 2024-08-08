package com.project.bsky.util;

import java.util.Random;
import java.util.UUID;

public class RandomNumber {
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static String generateRandomClaimNo(){
        return "CL" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }

}
