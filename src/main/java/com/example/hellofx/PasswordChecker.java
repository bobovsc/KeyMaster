package com.example.hellofx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class PasswordChecker {

    private static final String HIBP_API_URL = "https://api.pwnedpasswords.com/range/";

    public static boolean isPasswordPwned(String password) {
        try {
            String sha1Hash = sha1Hash(password);
            String hashPrefix = sha1Hash.substring(0, 5); // Use the first 5 characters of the hash

            // Make an HTTP request to the HIBP API
            String apiUrl = HIBP_API_URL + hashPrefix;
            String response = sendHttpRequest(apiUrl);

            // Check if the password hash suffix is in the response
            return response.contains(sha1Hash.substring(5).toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle errors appropriately in your application
        }
    }

    private static String sha1Hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }

        return hexString.toString().toUpperCase();
    }

    private static String sendHttpRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            return content.toString();
        } finally {
            connection.disconnect();
        }
    }

    public List<String> checkPasswords(List<PasswordEntry> entries) {
        List<String> pwnedPasswords = new ArrayList<>();

        for (PasswordEntry entry : entries) {
            String password = entry.getPassword();
            if (isPasswordPwned(password)) {
                pwnedPasswords.add(password);
            }
        }

        return pwnedPasswords;
    }
}
