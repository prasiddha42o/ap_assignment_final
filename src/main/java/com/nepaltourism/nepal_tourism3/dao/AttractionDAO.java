package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.Attraction;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttractionDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/attractions.txt";
    private static final String DELIMITER = ";;"; // More robust delimiter

    public static List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();

        // Create file if it doesn't exist
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                AlertUtil.showErrorAlert("Error creating attractions file: " + e.getMessage(), "Only administrators can access festival management");
                return attractions;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            attractions = reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(AttractionDAO::parseAttraction)
                    .filter(attraction -> attraction != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error reading attractions: " + e.getMessage(), "Only administrators can access festival management");
        }
        return attractions;
    }
    public static int getAttractionCount() {
        return getAllAttractions().size();
    }

    private static Attraction parseAttraction(String line) {
        try {
            return Attraction.fromString(line);
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error parsing attraction: " + line, "Only administrators can access festival management");
            return null;
        }
    }

    public static boolean addAttraction(Attraction attraction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(attraction.toString(DELIMITER));
            writer.newLine();
            return true;
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error adding attraction: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        }
    }

    public static boolean updateAttraction(Attraction updatedAttraction) {
        List<Attraction> attractions = getAllAttractions();
        boolean found = false;

        for (int i = 0; i < attractions.size(); i++) {
            if (attractions.get(i).getId() == updatedAttraction.getId()) {
                attractions.set(i, updatedAttraction);
                found = true;
                break;
            }
        }

        if (!found) {
            AlertUtil.showErrorAlert("Attraction not found for update", "Only administrators can access festival management");
            return false;
        }

        return saveAllAttractions(attractions);
    }

    public static boolean deleteAttraction(int id) {
        List<Attraction> attractions = getAllAttractions();
        int initialSize = attractions.size();

        attractions = attractions.stream()
                .filter(a -> a.getId() != id)
                .collect(Collectors.toList());

        if (attractions.size() == initialSize) {
            AlertUtil.showErrorAlert("Attraction not found for deletion", "Only administrators can access festival management");
            return false;
        }

        return saveAllAttractions(attractions);
    }

    private static boolean saveAllAttractions(List<Attraction> attractions) {
        File backup = new File(FILE_PATH + ".bak");
        File original = new File(FILE_PATH);

        try {
            // Create backup
            if (original.exists()) {
                original.renameTo(backup);
            }

            // Write new file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (Attraction a : attractions) {
                    writer.write(a.toString(DELIMITER));
                    writer.newLine();
                }
            }

            // Delete backup if successful
            if (backup.exists()) {
                backup.delete();
            }
            return true;
        } catch (IOException e) {
            // Restore from backup if error occurs
            if (backup.exists()) {
                backup.renameTo(original);
            }
            AlertUtil.showErrorAlert("Error saving attractions: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        }
    }

    public static int getNextId() {
        return getAllAttractions().stream()
                .mapToInt(Attraction::getId)
                .max()
                .orElse(0) + 1;
    }
}