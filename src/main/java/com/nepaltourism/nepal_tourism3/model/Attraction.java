package com.nepaltourism.nepal_tourism3.model;

public class Attraction {
    private int id;
    private String name;
    private String type;
    private String location;
    private String difficulty;
    private int altitude;
    private double price;
    private int duration;
    private String bestSeason;
    private boolean requiresGuide;
    private String description;

    // Constants
    private static final String DELIMITER = ";;";
    private static final String DELIMITER_REPLACEMENT = "__DELIM__";

    // Constructor
    public Attraction(int id, String name, String type, String location,
                      String difficulty, int altitude, double price, int duration,
                      String bestSeason, boolean requiresGuide, String description) {
        setId(id);
        setName(name);
        setType(type);
        setLocation(location);
        setDifficulty(difficulty);
        setAltitude(altitude);
        setPrice(price);
        setDuration(duration);
        setBestSeason(bestSeason);
        setRequiresGuide(requiresGuide);
        setDescription(description);
    }

    // Getters and Setters with validation
    public int getId() { return id; }
    public void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("ID cannot be negative");
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = (name == null) ? "" : name.trim();
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = (type == null) ? "" : type.trim();
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = (location == null) ? "" : location.trim();
    }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) {
        this.difficulty = (difficulty == null) ? "" : difficulty.trim();
    }

    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) {
        if(altitude < 0) throw new IllegalArgumentException("Altitude cannot be negative");
        this.altitude = altitude;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if(price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) {
        if(duration < 0) throw new IllegalArgumentException("Duration cannot be negative");
        this.duration = duration;
    }

    public String getBestSeason() { return bestSeason; }
    public void setBestSeason(String bestSeason) {
        this.bestSeason = (bestSeason == null) ? "" : bestSeason.trim();
    }

    public boolean isRequiresGuide() { return requiresGuide; }
    public void setRequiresGuide(boolean requiresGuide) {
        this.requiresGuide = requiresGuide;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = (description == null) ? "" : description.trim();
    }

    // For file storage
    @Override
    public String toString() {
        return toString(DELIMITER);
    }

    public String toString(String delimiter) {
        return id + delimiter +
                sanitize(name) + delimiter +
                sanitize(type) + delimiter +
                sanitize(location) + delimiter +
                sanitize(difficulty) + delimiter +
                altitude + delimiter +
                price + delimiter +
                duration + delimiter +
                sanitize(bestSeason) + delimiter +
                requiresGuide + delimiter +
                sanitize(description);
    }

    // Static method to create Attraction from string
    public static Attraction fromString(String str) {
        String[] parts = str.split(DELIMITER, -1); // -1 keeps empty values
        if (parts.length != 11) {
            throw new IllegalArgumentException("Invalid attraction data format. Expected 11 parts, got " + parts.length);
        }

        try {
            return new Attraction(
                    Integer.parseInt(parts[0]),
                    unsanitize(parts[1]),
                    unsanitize(parts[2]),
                    unsanitize(parts[3]),
                    unsanitize(parts[4]),
                    Integer.parseInt(parts[5]),
                    Double.parseDouble(parts[6]),
                    Integer.parseInt(parts[7]),
                    unsanitize(parts[8]),
                    Boolean.parseBoolean(parts[9]),
                    unsanitize(parts[10])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing numeric values: " + e.getMessage());
        }
    }

    // Helper methods for handling special characters
    private static String sanitize(String input) {
        if (input == null) return "";
        return input.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private static String unsanitize(String input) {
        if (input == null) return "";
        return input.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace(DELIMITER_REPLACEMENT, DELIMITER);
    }
}