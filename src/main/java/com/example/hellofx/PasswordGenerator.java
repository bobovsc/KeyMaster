package com.example.hellofx;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_=+[]{}?";
    private static final String ALL_CHARS = LOWERCASE_CHARS + UPPERCASE_CHARS + SPECIAL_CHARS;

    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        int specialCharCount = (int) (length * 0.1); //fix it ca nu mere bine

        //special characters
        for (int i = 0; i < specialCharCount; i++) {
            int randomIndex = random.nextInt(SPECIAL_CHARS.length());
            password.append(SPECIAL_CHARS.charAt(randomIndex));
        }

        //remaining characters
        for (int i = specialCharCount; i < length; i++) {
            int randomIndex = random.nextInt(ALL_CHARS.length());
            password.append(ALL_CHARS.charAt(randomIndex));
        }

        //randomness
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = passwordArray[index];
            passwordArray[index] = passwordArray[i];
            passwordArray[i] = temp;
        }

        return new String(passwordArray);
    }
}
