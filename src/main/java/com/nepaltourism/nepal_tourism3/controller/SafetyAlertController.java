package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.SafetyAlertDAO;
import com.nepaltourism.nepal_tourism3.model.SafetyAlert;
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
import java.time.LocalDate;

public class SafetyAlertController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> alertTypeCombo;
    @FXML private ComboBox<String> severityCombo;
    @FXML private TextField affectedAreasField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox activeCheckBox;

    @FXML private TableView<SafetyAlert> alertTable;
    @FXML private TableColumn<SafetyAlert, String> titleColumn;
    @FXML private TableColumn<SafetyAlert, String> typeColumn;
    @FXML private TableColumn<SafetyAlert, String> severityColumn;
    @FXML private TableColumn<SafetyAlert, String> areasColumn;
    @FXML private TableColumn<SafetyAlert, LocalDate> startDateColumn;
    @FXML private TableColumn<SafetyAlert, LocalDate> endDateColumn;

    private ObservableList<SafetyAlert> alertList = FXCollections.observableArrayList();
    private SafetyAlert selectedAlert = null;

    @FXML
    public void initialize() {
        // Initialize combo boxes
        alertTypeCombo.getItems().addAll("Flood", "Earthquake", "Landslide", "Avalanche", "Storm", "Other");
        severityCombo.getItems().addAll("Low", "Medium", "High", "Critical");

        // Set up table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("alertType"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        areasColumn.setCellValueFactory(new PropertyValueFactory<>("affectedAreas"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Load alerts
        refreshAlerts();

        // Set up table selection listener
        alertTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedAlert = newSelection;
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });
    }

    private void refreshAlerts() {
        alertList.setAll(SafetyAlertDAO.getAllAlerts());
        alertTable.setItems(alertList);
    }

    private void populateForm(SafetyAlert alert) {
        titleField.setText(alert.getTitle());
        descriptionArea.setText(alert.getDescription());
        alertTypeCombo.setValue(alert.getAlertType());
        severityCombo.setValue(alert.getSeverity());
        affectedAreasField.setText(alert.getAffectedAreas());
        startDatePicker.setValue(alert.getStartDate());
        endDatePicker.setValue(alert.getEndDate());
        activeCheckBox.setSelected(alert.isActive());
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        alertTypeCombo.getSelectionModel().clearSelection();
        severityCombo.getSelectionModel().clearSelection();
        affectedAreasField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        activeCheckBox.setSelected(false);
        selectedAlert = null;
    }

    private boolean validateForm() {
        if (titleField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter alert title");
            return false;
        }
        if (alertTypeCombo.getValue() == null) {
            showAlert("Validation Error", "Please select alert type");
            return false;
        }
        if (startDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select start date");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddAlert(ActionEvent event) {
        if (validateForm()) {
            SafetyAlert alert = new SafetyAlert(
                    0, // ID will be assigned by DAO
                    titleField.getText(),
                    descriptionArea.getText(),
                    alertTypeCombo.getValue(),
                    severityCombo.getValue(),
                    affectedAreasField.getText(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue() != null ? endDatePicker.getValue() : startDatePicker.getValue(),
                    activeCheckBox.isSelected()
            );

            if (SafetyAlertDAO.addAlert(alert)) {
                refreshAlerts();
                clearForm();
            }
        }
    }

    @FXML
    private void handleUpdateAlert(ActionEvent event) {
        if (selectedAlert != null && validateForm()) {
            selectedAlert.setTitle(titleField.getText());
            selectedAlert.setDescription(descriptionArea.getText());
            selectedAlert.setAlertType(alertTypeCombo.getValue());
            selectedAlert.setSeverity(severityCombo.getValue());
            selectedAlert.setAffectedAreas(affectedAreasField.getText());
            selectedAlert.setStartDate(startDatePicker.getValue());
            selectedAlert.setEndDate(endDatePicker.getValue() != null ? endDatePicker.getValue() : startDatePicker.getValue());
            selectedAlert.setActive(activeCheckBox.isSelected());

            if (SafetyAlertDAO.updateAlert(selectedAlert)) {
                refreshAlerts();
                clearForm();
            }
        }
    }

    @FXML
    private void handleDeleteAlert(ActionEvent event) {
        if (selectedAlert != null) {
            if (SafetyAlertDAO.deleteAlert(selectedAlert.getId())) {
                refreshAlerts();
                clearForm();
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
            showAlert("Navigation Error", "Failed to return to dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        clearForm();
    }
}