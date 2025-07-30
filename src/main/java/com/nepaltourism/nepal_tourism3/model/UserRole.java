package com.nepaltourism.nepal_tourism3.model;

public enum UserRole {
    ADMIN("admin"),
    GUIDE("guide"),
    TOURIST("tourist");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static UserRole fromString(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for role: " + roleName);
    }
}