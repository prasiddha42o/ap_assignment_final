package com.nepaltourism.nepal_tourism3.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;

import java.io.IOException;

public class EmergencyManagementController {
    @FXML private VBox contentArea;
    @FXML private Button backBtn;
    @FXML private Button contactsBtn;
    @FXML private Button reportBtn;

    @FXML
    public void initialize() {
        setupEmergencyContacts(); // Show contacts by default
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to return to dashboard: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void setupEmergencyContacts() {
        contentArea.getChildren().clear();

        // Emergency contacts list
        String[] contacts = {
                "Police: 100",
                "Tourist Police: +977-1-4247041",
                "Ambulance: 102",
                "Fire Department: 101",
                "Mountain Rescue: +977-1-4374625",
                "Embassy Contacts: View at reception"
        };

        Label title = new Label("Emergency Contacts");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentArea.getChildren().add(title);

        for (String contact : contacts) {
            Label contactLabel = new Label(contact);
            contactLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 0;");
            contentArea.getChildren().add(contactLabel);
        }
    }

    @FXML
    private void setupEmergencyReport() {
        contentArea.getChildren().clear();

        Label title = new Label("Report Emergency");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");

        ComboBox<String> emergencyType = new ComboBox<>();
        emergencyType.getItems().addAll(
                "Medical Emergency",
                "Natural Disaster",
                "Accident",
                "Crime",
                "Lost/Missing Person",
                "Other"
        );
        emergencyType.setPromptText("Select Emergency Type");

        TextArea detailsArea = new TextArea();
        detailsArea.setPromptText("Provide details about the emergency...");
        detailsArea.setPrefRowCount(3);

        Button submitBtn = new Button("Submit Report");
        submitBtn.setOnAction(e -> {
            if (validateReport(nameField.getText(), emergencyType.getValue(), detailsArea.getText())) {
                AlertUtil.showSuccessAlert("Emergency report submitted!\nHelp is on the way.");
                setupEmergencyContacts(); // Return to contacts view
            }
        });

        contentArea.getChildren().addAll(title, nameField, emergencyType, detailsArea, submitBtn);
    }

    private boolean validateReport(String name, String type, String details) {
        if (name == null || name.trim().isEmpty()) {
            AlertUtil.showErrorAlert("Please enter your name", "Only administrators can access festival management");
            return false;
        }
        if (type == null) {
            AlertUtil.showErrorAlert("Please select emergency type", "Only administrators can access festival management");
            return false;
        }
        if (details == null || details.trim().isEmpty()) {
            AlertUtil.showErrorAlert("Please provide emergency details", "Only administrators can access festival management");
            return false;
        }
        return true;
    }
}