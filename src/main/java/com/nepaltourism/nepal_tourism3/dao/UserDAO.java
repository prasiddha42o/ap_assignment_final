package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.User;
import com.nepaltourism.nepal_tourism3.util.ValidationUtil;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/user.txt";
    private static final List<String> VALID_NATIONALITIES = Arrays.asList(
            "Nepali", "Indian", "Chinese", "American", "British",
            "Australian", "Japanese", "Korean", "French", "German"
    );

    public static String registerUser(User user) {
        try {
            // Validate fields
            String validationError = validateUserFields(user);
            if (validationError != null) return validationError;

            // Check if user exists
            if (usernameExists(user.getUsername())) {
                return "Username already exists";
            }
            if (emailExists(user.getEmail())) {
                return "Email already registered";
            }

            // Create user record
            String userRecord = String.join(",",
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().getRoleName(),
                    user.getFullName(),
                    user.getContactNumber(),
                    user.getEmergencyContact(),
                    user.getNationality(),
                    user.getPassportNumber(),
                    user.getEmergencyContactName()
            );

            // Write to file
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            Files.write(path, (userRecord + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            return null; // Success
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving user data: " + e.getMessage();
        }
    }

    private static String validateUserFields(User user) {
        if (!ValidationUtil.isValidUsername(user.getUsername())) {
            return "Username must be 3-20 alphanumeric characters";
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }
        if (user.getPassword().length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!VALID_NATIONALITIES.contains(user.getNationality())) {
            return "Please select a valid nationality";
        }
        return null;
    }

    private static boolean usernameExists(String username) throws IOException {
        if (!Files.exists(Paths.get(FILE_PATH))) return false;
        return Files.lines(Paths.get(FILE_PATH))
                .anyMatch(line -> line.startsWith(username + ","));
    }

    private static boolean emailExists(String email) throws IOException {
        if (!Files.exists(Paths.get(FILE_PATH))) return false;
        return Files.lines(Paths.get(FILE_PATH))
                .anyMatch(line -> line.split(",").length > 1 && line.split(",")[1].equals(email));
    }

    public static Optional<User> validateLogin(String username, String password) {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return Optional.empty();
            }

            return Files.lines(Paths.get(FILE_PATH))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 3) // Ensure we have at least username, email, password
                    .filter(parts -> parts[0].equals(username) && parts[2].equals(password))
                    .findFirst()
                    .map(parts -> {
                        // Create User object from the stored data
                        String role = parts.length > 3 ? parts[3] : "tourist"; // Default role if missing
                        String fullName = parts.length > 4 ? parts[4] : "";
                        String contactNumber = parts.length > 5 ? parts[5] : "";
                        String emergencyContact = parts.length > 6 ? parts[6] : "";
                        String nationality = parts.length > 7 ? parts[7] : "";
                        String passportNumber = parts.length > 8 ? parts[8] : "";
                        String emergencyContactName = parts.length > 9 ? parts[9] : "";

                        return new User(
                                parts[0], // username
                                parts[1], // email
                                parts[2], // password
                                role,
                                fullName,
                                contactNumber,
                                emergencyContact,
                                nationality,
                                passportNumber,
                                emergencyContactName
                        );
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}