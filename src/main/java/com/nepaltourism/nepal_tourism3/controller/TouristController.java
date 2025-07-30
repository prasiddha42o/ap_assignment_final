package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.TouristDAO;
import com.nepaltourism.nepal_tourism3.model.Tourist;
import com.nepaltourism.nepal_tourism3.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class TouristController {
    @FXML private TextField nameField;
    @FXML private ComboBox<String> nationalityComboBox;
    @FXML private TextField contactField;
    @FXML private TextField emailField;
    @FXML private TextField emergencyContactField;
    @FXML private TextField emergencyNameField;
    @FXML private TextField passportField;
    @FXML private TextArea addressArea;

    @FXML private TableView<Tourist> touristTable;
    @FXML private TableColumn<Tourist, String> nameColumn;
    @FXML private TableColumn<Tourist, String> nationalityColumn;
    @FXML private TableColumn<Tourist, String> contactColumn;
    @FXML private TableColumn<Tourist, String> emailColumn;
    @FXML private TableColumn<Tourist, String> registrationColumn;
    @FXML private Button backButton;

    private ObservableList<Tourist> touristList = FXCollections.observableArrayList();
    private Tourist selectedTourist = null;

    @FXML
    public void initialize() {
        // Initialize nationality combo box
        nationalityComboBox.getItems().addAll(
                "Nepali", "Indian", "Chinese", "American", "British",
                "Australian", "Japanese", "Korean", "French", "German"
        );

        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Load tourists
        refreshTourists();

        // Set up table selection listener
        touristTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedTourist = newSelection;
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });
    }

    private void refreshTourists() {
        touristList.setAll(TouristDAO.getAllTourists());
        touristTable.setItems(touristList);
    }

    private void populateForm(Tourist tourist) {
        nameField.setText(tourist.getName());
        nationalityComboBox.setValue(tourist.getNationality());
        contactField.setText(tourist.getContact());
        emailField.setText(tourist.getEmail());
        emergencyContactField.setText(tourist.getEmergencyContact());
        emergencyNameField.setText(tourist.getEmergencyName());
        passportField.setText(tourist.getPassport());
        addressArea.setText(tourist.getAddress());
    }

    void clearForm() {
        nameField.clear();
        nationalityComboBox.getSelectionModel().clearSelection();
        contactField.clear();
        emailField.clear();
        emergencyContactField.clear();
        emergencyNameField.clear();
        passportField.clear();
        addressArea.clear();
        selectedTourist = null;
    }

    private boolean validateForm() {
        if (nameField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter tourist name", Alert.AlertType.ERROR);
            return false;
        }
        if (nationalityComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select nationality", Alert.AlertType.ERROR);
            return false;
        }
        if (contactField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter contact number", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddTourist(ActionEvent event) {
        if (validateForm()) {
            try {
                Tourist tourist = new Tourist(
                        0,
                        nameField.getText(),
                        nationalityComboBox.getValue(),
                        contactField.getText(),
                        emailField.getText(),
                        emergencyContactField.getText(),
                        emergencyNameField.getText(),
                        passportField.getText(),
                        addressArea.getText()
                );
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }

                if (TouristDAO.addTourist(tourist)) {
                    // Force refresh the table
                    touristList.clear();
                    touristList.addAll(TouristDAO.getAllTourists());
                    touristTable.refresh();

                    clearForm();
                    showSuccessAlert("Tourist added successfully!");
                } else {
                    showAlert("Error", "Failed to save tourist", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleUpdateTourist(ActionEvent event) {
        if (selectedTourist != null && validateForm()) {
            selectedTourist.setName(nameField.getText());
            selectedTourist.setNationality(nationalityComboBox.getValue());
            selectedTourist.setContact(contactField.getText());
            selectedTourist.setEmail(emailField.getText());
            selectedTourist.setEmergencyContact(emergencyContactField.getText());
            selectedTourist.setEmergencyName(emergencyNameField.getText());
            selectedTourist.setPassport(passportField.getText());
            selectedTourist.setAddress(addressArea.getText());

            if (TouristDAO.updateTourist(selectedTourist)) {
                refreshTourists();
                clearForm();
            }
            AdminDashboardController dashboardController = SessionManager.getDashboardController();
            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }
        }
    }

    @FXML
    private void handleDeleteTourist(ActionEvent event) {
        if (selectedTourist != null) {
            if (TouristDAO.deleteTourist(selectedTourist.getId())) {
                refreshTourists();
                clearForm();
            }
            AdminDashboardController dashboardController = SessionManager.getDashboardController();
            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }
        }
    }
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to return to dashboard: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void handleClearForm(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void searchTourists(ActionEvent event) {
        // Implement search functionality if needed
    }
}