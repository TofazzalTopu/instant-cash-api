package com.info.api.util;

import java.io.IOException;
import java.util.Base64;

public class PasswordUtil {

    public static void main(String[] args) throws IOException {
        System.out.println(generateBase64Hash("AE90005555", "Qwe_123!"));
    }
    public static String generateBase64Hash(String userId, String password) {
        String originalInput = userId + ":" + password;
        return Base64.getEncoder().encodeToString(originalInput.getBytes());
    }
}
