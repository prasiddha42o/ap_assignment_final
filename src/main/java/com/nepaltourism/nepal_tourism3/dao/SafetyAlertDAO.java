package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.SafetyAlert;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class SafetyAlertDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/alerts.txt";
    private static int lastId = 0;

    static {
        initializeLastId();
    }

    private static void initializeLastId() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            if (!lines.isEmpty()) {
                String[] parts = lines.get(lines.size() - 1).split(",");
                lastId = Integer.parseInt(parts[0]);
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
    }

    public static List<SafetyAlert> getAllAlerts() {
        List<SafetyAlert> alerts = new ArrayList<>();
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return alerts;
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    alerts.add(new SafetyAlert(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5],
                            LocalDate.parse(parts[6]),
                            LocalDate.parse(parts[7]),
                            Boolean.parseBoolean(parts[8])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alerts;
    }

    public static boolean addAlert(SafetyAlert alert) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                if (alert.getId() == 0) {
                    alert.setId(++lastId);
                }

                String alertData = String.join(",",
                        String.valueOf(alert.getId()),
                        alert.getTitle(),
                        alert.getDescription(),
                        alert.getAlertType(),
                        alert.getSeverity(),
                        alert.getAffectedAreas(),
                        alert.getStartDate().toString(),
                        alert.getEndDate().toString(),
                        String.valueOf(alert.isActive())
                );

                writer.write(alertData);
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateAlert(SafetyAlert alert) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return false;
            }

            List<String> lines = Files.readAllLines(path);
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(String.valueOf(alert.getId()))) {
                    newLines.add(String.join(",",
                            String.valueOf(alert.getId()),
                            alert.getTitle(),
                            alert.getDescription(),
                            alert.getAlertType(),
                            alert.getSeverity(),
                            alert.getAffectedAreas(),
                            alert.getStartDate().toString(),
                            alert.getEndDate().toString(),
                            String.valueOf(alert.isActive())
                    ));
                } else {
                    newLines.add(line);
                }
            }

            Files.write(path, newLines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAlert(int id) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return false;
            }

            List<String> lines = Files.readAllLines(path);
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].equals(String.valueOf(id))) {
                    newLines.add(line);
                }
            }

            Files.write(path, newLines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}