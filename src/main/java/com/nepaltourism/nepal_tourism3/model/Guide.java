package com.nepaltourism.nepal_tourism3.model;

public class Guide {
    private int id;
    private String name;
    private String languages;
    private int experience;
    private String contact;
    private String email;
    private String specialization;
    private String license;
    private double rating;
    private boolean available;

    public Guide(int id, String name, String languages, int experience, String contact,
                 String email, String specialization, String license, double rating, boolean available) {
        this.id = id;
        this.name = name;
        this.languages = languages;
        this.experience = experience;
        this.contact = contact;
        this.email = email;
        this.specialization = specialization;
        this.license = license;
        this.rating = rating;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
