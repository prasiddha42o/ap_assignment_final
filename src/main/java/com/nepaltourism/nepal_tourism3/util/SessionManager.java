package com.nepaltourism.nepal_tourism3.util;

import com.nepaltourism.nepal_tourism3.controller.AdminDashboardController;
import com.nepaltourism.nepal_tourism3.model.User;

public class SessionManager {
    private static User currentUser;
    private static AdminDashboardController dashboardController;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void setDashboardController(AdminDashboardController controller) {
        dashboardController = controller;
    }

    public static AdminDashboardController getDashboardController() {
        return dashboardController;
    }
}