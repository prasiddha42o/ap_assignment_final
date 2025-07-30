package com.nepaltourism.nepal_tourism3.model;

import java.time.LocalDate;

public class Tourist {
    private int id;
    private String name;
    private String nationality;
    private String contact;
    private String email;
    private String emergencyContact;
    private String emergencyName;
    private String passport;
    private String address;
    private LocalDate registrationDate;

    // Constructor with all fields
    public Tourist(int id, String name, String nationality, String contact, String email,
                   String emergencyContact, String emergencyName, String passport, String address) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.contact = contact;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.emergencyName = emergencyName;
        this.passport = passport;
        this.address = address;
        this.registrationDate = LocalDate.now();
    }

    // Getters and Setters
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "Tourist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", emergencyName='" + emergencyName + '\'' +
                ", passport='" + passport + '\'' +
                ", address='" + address + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}