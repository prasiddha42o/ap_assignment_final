package com.nepaltourism.nepal_tourism3.model;

import java.time.LocalDate;

public class FestivalDiscount {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountPercent;
    private String applicableAttractions; // "ALL" or comma-separated IDs

    // Constants for file storage
    private static final String DELIMITER = ";;";
    private static final String DELIMITER_REPLACEMENT = "__DELIM__";

    public FestivalDiscount(int id, String name, LocalDate startDate, LocalDate endDate,
                            double discountPercent) {
        setId(id);
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setDiscountPercent(discountPercent);
        setApplicableAttractions(applicableAttractions);
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

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) {
        if(startDate == null) throw new IllegalArgumentException("Start date cannot be null");
        this.startDate = startDate;
    }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) {
        if(endDate == null) throw new IllegalArgumentException("End date cannot be null");
        if(startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) {
        if(discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0-100");
        }
        this.discountPercent = discountPercent;
    }

    public String getApplicableAttractions() { return applicableAttractions; }
    public void setApplicableAttractions(String applicableAttractions) {
        this.applicableAttractions = (applicableAttractions == null) ? "" : applicableAttractions.trim();
    }

    // File storage methods
    @Override
    public String toString() {
        return toString(DELIMITER);
    }

    public String toString(String delimiter) {
        return id + delimiter +
                sanitize(name) + delimiter +
                startDate + delimiter +
                endDate + delimiter +
                discountPercent + delimiter +
                sanitize(applicableAttractions);
    }

    public static FestivalDiscount fromString(String str) {
        String[] parts = str.split(DELIMITER, -1);
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid festival data format");
        }

        try {
            return new FestivalDiscount(
                    Integer.parseInt(parts[0]),
                    unsanitize(parts[1]),
                    LocalDate.parse(parts[2]),
                    LocalDate.parse(parts[3]),
                    Double.parseDouble(parts[4])
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing festival: " + e.getMessage());
        }
    }

    private static String sanitize(String input) {
        if (input == null) return "";
        return input.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private static String unsanitize(String input) {
        if (input == null) return "";
        return input.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }
}