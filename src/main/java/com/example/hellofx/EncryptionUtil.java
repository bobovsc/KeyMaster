package com.example.hellofx;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    public static String encrypt(String password) {
        try {
            String key = "verysecretkey";
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            char[] passwordChars = password.toCharArray();
            byte[] salt = "salt".getBytes();
            PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, 65536, 128);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] encryptedText = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));

            return new String(encryptedText, StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

