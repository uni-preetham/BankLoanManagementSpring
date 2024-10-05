package com.crimsonlogic.bankloanmanagementsystem.util;

import java.util.Random;

public class IdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 10;
 
    public static String generateId(String prefix) {
        StringBuilder id = new StringBuilder(prefix);
        Random random = new Random();
        for (int i = 0; i < ID_LENGTH; i++) {
            id.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return id.toString();
    }
}
