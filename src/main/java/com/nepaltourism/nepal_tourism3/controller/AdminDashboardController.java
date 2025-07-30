package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.*;
import com.nepaltourism.nepal_tourism3.model.SafetyAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.nepaltourism.nepal_tourism3.util.SessionManager;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import com.nepaltourism.nepal_tourism3.model.User;


import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AdminDashboardController {
    @FXML private Label totalTouristsLabel;
    @FXML private Label totalGuidesLabel;
    @FXML private Label totalAttractionsLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private ListView<String> alertsListView;
    @FXML private Button touristsButton;
    @FXML private Button guidesButton;
    @FXML private Button attractionsButton;
    @FXML private Button bookingsButton;
    @FXML private Button festivalsButton;
    @FXML private Button emergencyButton;
    @FXML private ComboBox<String> languageComboBox;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            setupRoleBasedAccess(currentUser.getRole());
        }
        updateDashboardCounts();
        loadSafetyAlerts();

    }

    private void setupRoleBasedAccess(User.Role role) {
        // Reset all buttons to visible first
        touristsButton.setVisible(true);
        guidesButton.setVisible(true);
        attractionsButton.setVisible(true);
        bookingsButton.setVisible(true);
        festivalsButton.setVisible(true);
        emergencyButton.setVisible(true);

        // Apply restrictions based on role
        switch (role) {
            case ADMIN:
                // Admin can see everything - no changes needed
                break;
            case GUIDE:
                // Guides can only access Guide Management and Attraction Management
                touristsButton.setVisible(false);
                bookingsButton.setVisible(false);
                festivalsButton.setVisible(false);
                emergencyButton.setVisible(false);
                break;
            case TOURIST:
                // Tourists can only access Tourist Management and Booking Management
                guidesButton.setVisible(false);
                attractionsButton.setVisible(false);
                festivalsButton.setVisible(false);
                emergencyButton.setVisible(false);
                break;
        }
    }


        private void updateDashboardCounts() {
        totalTouristsLabel.setText(String.valueOf(TouristDAO.getTouristCount()));
        totalGuidesLabel.setText(String.valueOf(GuideDAO.getGuideCount()));
        totalAttractionsLabel.setText(String.valueOf(AttractionDAO.getAttractionCount()));
        totalBookingsLabel.setText(String.valueOf(BookingDAO.getBookingCount()));
    }
    public void refreshDashboard() {
        updateDashboardCounts();
    }
    private void loadSafetyAlerts() {
        List<SafetyAlert> alerts = SafetyAlertDAO.getAllAlerts();
        ObservableList<String> alertItems = FXCollections.observableArrayList();

        for (SafetyAlert alert : alerts) {
            if (alert.isActive()) {
                String alertText = String.format("[%s] %s - %s (%s)",
                        alert.getAlertType(),
                        alert.getTitle(),
                        alert.getAffectedAreas(),
                        alert.getSeverity());
                alertItems.add(alertText);
            }
        }

        if (alertItems.isEmpty()) {
            alertItems.add("No active safety alerts at this time.");
        }

        alertsListView.setItems(alertItems);
        alertsListView.setStyle("-fx-background-color: #fff3e0; -fx-border-color: #ffb74d; -fx-border-width: 1px;");
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/Login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login - Nepal Tourism Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Logout Failed");
            alert.setHeaderText("Unable to load login screen");
            alert.setContentText("Please contact administrator.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleViewAllAttractions() {
        User currentUser = SessionManager.getCurrentUser();
        if (!currentUser.isAdmin() && !currentUser.isGuide()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/AttractionManagement.fxml"));
            Parent root = loader.load();

            AttractionController controller = loader.getController();
            controller.initializeViewAllMode();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setTitle("All Attractions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load attractions view: " + e.getMessage(), "Only administrators can access festival management");
        }
    }
    @FXML
    private void handleEmergencyManagement() {
        if (!SessionManager.getCurrentUser().isAdmin()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/EmergencyManagement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Emergency Management");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load emergency management: " + e.getMessage(), "Only administrators can access festival management");
        }
    }
    @FXML
    private void handleBookingManagement(ActionEvent event) {
        User currentUser = SessionManager.getCurrentUser();
        if (!currentUser.isAdmin() && !currentUser.isTourist()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/nepaltourism/fxml/fxml/BookingManagement.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Booking Management");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load booking management: " + e.getMessage(), "Only administrators can access festival management");
        }
    }
    @FXML
    private void handleTouristManagement(ActionEvent event) {
        if (!SessionManager.getCurrentUser().isAdmin()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/nepaltourism/fxml/fxml/TouristManagement.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tourist Management");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load tourist management: " + e.getMessage(), "Only administrators can access festival management");
        }
    }
    @FXML
    private void handleGuideManagement(ActionEvent event) {
        User currentUser = SessionManager.getCurrentUser();
        if (!currentUser.isAdmin() && !currentUser.isGuide()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/nepaltourism/fxml/fxml/GuideManagement.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Guide Management");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load guide management: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void handleSafetyAlerts(ActionEvent event) {
        if (!SessionManager.getCurrentUser().isAdmin()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/nepaltourism/fxml/fxml/SafetyAlerts.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Safety Alert Management");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to load safety alerts: " + e.getMessage(), "Only administrators can access festival management");
        }
    }
    @FXML
    private void handleFestivalDiscountManagement(ActionEvent event) {
        if (!SessionManager.getCurrentUser().isAdmin()) {
            AlertUtil.showErrorAlert("Access Denied", "Only administrators can access festival management");
            return;
        }
        try {
            URL url = getClass().getResource("/com/nepaltourism/fxml/fxml/FestivalManagement.fxml");
            if (url == null) {
                throw new IOException("File not found at: /com/nepaltourism/fxml/fxml/FestivalManagement.fxml");
            }

            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Festival Discount Management");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showErrorAlert("FXML Loading Error: " + e.getMessage(), "Only administrators can access festival management");
        }
    }



    @FXML
    private void handleCreateNewBooking(ActionEvent event) {
        handleBookingManagement(event);
    }

}
