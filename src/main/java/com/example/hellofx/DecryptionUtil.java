package com.example.hellofx;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class DecryptionUtil {

    public static String decrypt(String encryptedPassword) {
        try {
            String key = "verysecretkey";
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] salt = "salt".getBytes();
            PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 65536, 128);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret);

            byte[] decryptedText = cipher.doFinal(encryptedPassword.getBytes(StandardCharsets.ISO_8859_1));
            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
