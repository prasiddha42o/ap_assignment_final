package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.FestivalDiscount;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FestivalDiscountDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/discounts.txt";
    private static final String DELIMITER = ";;";

    public static List<FestivalDiscount> getAllFestivals() {
        List<FestivalDiscount> festivals = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createDirectories(Paths.get("data"));
                Files.createFile(Paths.get(FILE_PATH));
                return festivals;
            }

            festivals = Files.readAllLines(Paths.get(FILE_PATH)).stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(FestivalDiscount::fromString)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error reading festivals: " + e.getMessage(), "Only administrators can access festival management");
        }
        return festivals;
    }

    public static boolean addFestival(FestivalDiscount festival) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(FILE_PATH), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(festival.toString(DELIMITER));
            writer.newLine();
            return true;
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error adding festival: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        }
    }

    public static boolean updateFestival(FestivalDiscount updatedFestival) {
        List<FestivalDiscount> festivals = getAllFestivals();
        boolean found = false;

        for (int i = 0; i < festivals.size(); i++) {
            if (festivals.get(i).getId() == updatedFestival.getId()) {
                festivals.set(i, updatedFestival);
                found = true;
                break;
            }
        }

        if (!found) {
            AlertUtil.showErrorAlert("Festival not found for update", "Only administrators can access festival management");
            return false;
        }

        return saveAllFestivals(festivals);
    }

    public static boolean deleteFestival(int id) {
        List<FestivalDiscount> festivals = getAllFestivals();
        int initialSize = festivals.size();

        festivals = festivals.stream()
                .filter(f -> f.getId() != id)
                .collect(Collectors.toList());

        if (festivals.size() == initialSize) {
            AlertUtil.showErrorAlert("Festival not found for deletion", "Only administrators can access festival management");
            return false;
        }

        return saveAllFestivals(festivals);
    }

    private static boolean saveAllFestivals(List<FestivalDiscount> festivals) {
        Path tempFile = Paths.get(FILE_PATH + ".tmp");
        Path originalFile = Paths.get(FILE_PATH);

        try {
            // Write to temp file
            Files.write(tempFile,
                    festivals.stream()
                            .map(f -> f.toString(DELIMITER))
                            .collect(Collectors.toList()));

            // Replace original with temp
            Files.deleteIfExists(originalFile);
            Files.move(tempFile, originalFile);
            return true;
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error saving festivals: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        } finally {
            try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
        }
    }

    public static int getNextId() {
        return getAllFestivals().stream()
                .mapToInt(FestivalDiscount::getId)
                .max()
                .orElse(0) + 1;
    }

    public static double getCurrentDiscount(String attractionId) {
        LocalDate today = LocalDate.now();
        return getAllFestivals().stream()
                .filter(f -> !today.isBefore(f.getStartDate()) && !today.isAfter(f.getEndDate()))
                .filter(f -> f.getApplicableAttractions().equals("ALL") ||
                        f.getApplicableAttractions().contains(attractionId))
                .mapToDouble(FestivalDiscount::getDiscountPercent)
                .max()
                .orElse(0.0);
    }
}