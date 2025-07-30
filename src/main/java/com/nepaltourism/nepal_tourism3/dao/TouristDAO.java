package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.Tourist;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TouristDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/tourists.txt";
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
            // File doesn't exist yet, will be created on first save
        }
    }

    public static List<Tourist> getAllTourists() {
        List<Tourist> tourists = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    tourists.add(new Tourist(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7],
                            parts[8]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tourists;
    }
    public static int getTouristCount() {
        return getAllTourists().size();
    }


    public static boolean addTourist(Tourist tourist) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (tourist.getId() == 0) {
                tourist.setId(++lastId);
            }

            String touristData = String.join(",",
                    String.valueOf(tourist.getId()),
                    tourist.getName(),
                    tourist.getNationality(),
                    tourist.getContact(),
                    tourist.getEmail(),
                    tourist.getEmergencyContact(),
                    tourist.getEmergencyName(),
                    tourist.getPassport(),
                    tourist.getAddress()
            );

            writer.write(touristData);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTourist(Tourist tourist) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(String.valueOf(tourist.getId()))) {
                    newLines.add(String.join(",",
                            String.valueOf(tourist.getId()),
                            tourist.getName(),
                            tourist.getNationality(),
                            tourist.getContact(),
                            tourist.getEmail(),
                            tourist.getEmergencyContact(),
                            tourist.getEmergencyName(),
                            tourist.getPassport(),
                            tourist.getAddress()
                    ));
                } else {
                    newLines.add(line);
                }
            }

            Files.write(Paths.get(FILE_PATH), newLines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTourist(int id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].equals(String.valueOf(id))) {
                    newLines.add(line);
                }
            }

            Files.write(Paths.get(FILE_PATH), newLines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}