package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.Guide;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GuideDAO {
    private static final String FILE_PATH = "src/main/java/com/nepaltourism/nepal_tourism3/guides.txt";
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

    public static List<Guide> getAllGuides() {
        List<Guide> guides = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    guides.add(new Guide(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7],
                            Double.parseDouble(parts[8]),
                            Boolean.parseBoolean(parts[9])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guides;
    }
    public static int getGuideCount() {
        return getAllGuides().size();
    }

    public static boolean addGuide(Guide guide) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (guide.getId() == 0) {
                guide.setId(++lastId);
            }

            String guideData = String.join(",",
                    String.valueOf(guide.getId()),
                    guide.getName(),
                    guide.getLanguages(),
                    String.valueOf(guide.getExperience()),
                    guide.getContact(),
                    guide.getEmail(),
                    guide.getSpecialization(),
                    guide.getLicense(),
                    String.valueOf(guide.getRating()),
                    String.valueOf(guide.isAvailable())
            );

            writer.write(guideData);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateGuide(Guide guide) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(String.valueOf(guide.getId()))) {
                    newLines.add(String.join(",",
                            String.valueOf(guide.getId()),
                            guide.getName(),
                            guide.getLanguages(),
                            String.valueOf(guide.getExperience()),
                            guide.getContact(),
                            guide.getEmail(),
                            guide.getSpecialization(),
                            guide.getLicense(),
                            String.valueOf(guide.getRating()),
                            String.valueOf(guide.isAvailable())
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

    public static boolean deleteGuide(int id) {
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