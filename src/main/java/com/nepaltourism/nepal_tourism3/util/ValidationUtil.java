package com.nepaltourism.nepal_tourism3.util;

public class ValidationUtil {
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{3,20}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[\\p{L} .'-]{2,50}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\d{7,15}$");
    }
}