package com.nepaltourism.nepal_tourism3.model;

public class User {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String fullName;
    private String contactNumber;
    private String emergencyContact;
    private String nationality;
    private String passportNumber;
    private String emergencyContactName;
    // In your User.java class
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isGuide() {
        return this.role == Role.GUIDE;
    }

    public boolean isTourist() {
        return this.role == Role.TOURIST;
    }

    public enum Role {
        ADMIN("admin"),
        GUIDE("guide"),
        TOURIST("tourist");

        private final String roleName;

        Role(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        public static Role fromString(String roleName) {
            for (Role role : Role.values()) {
                if (role.roleName.equalsIgnoreCase(roleName)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
    }

    // Constructors
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password, String role) {
        this(username, email, password, Role.fromString(role));
    }

    public User(String username, String email, String password, String role,
                String fullName, String contactNumber, String emergencyContact,
                String nationality, String passportNumber, String emergencyContactName) {
        this(username, email, password, role);
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.emergencyContact = emergencyContact;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.emergencyContactName = emergencyContactName;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getContactNumber() { return contactNumber; }
    public String getEmergencyContact() { return emergencyContact; }
    public String getNationality() { return nationality; }
    public String getPassportNumber() { return passportNumber; }
    public String getEmergencyContactName() { return emergencyContactName; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
}