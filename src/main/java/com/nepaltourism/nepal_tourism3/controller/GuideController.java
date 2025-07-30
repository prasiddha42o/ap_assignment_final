package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.GuideDAO;
import com.nepaltourism.nepal_tourism3.model.Guide;
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

import static com.nepaltourism.nepal_tourism3.util.AlertUtil.showSuccessAlert;

public class GuideController {

    @FXML private TextField nameField;
    @FXML private TextField languagesField;
    @FXML private TextField experienceField;
    @FXML private TextField contactField;
    @FXML private TextField emailField;
    @FXML private TextField specializationField;
    @FXML private TextField licenseField;
    @FXML private Slider ratingSlider;

    @FXML private CheckBox availableCheckBox;

    @FXML private TableView<Guide> guideTable;
    @FXML private TableColumn<Guide, String> nameColumn;
    @FXML private TableColumn<Guide, String> languagesColumn;
    @FXML private TableColumn<Guide, String> contactColumn;
    @FXML private TableColumn<Guide, String> specializationColumn;
    @FXML private TableColumn<Guide, Double> ratingColumn;
    @FXML public TableColumn<Guide, Double> experienceColumn;

    private ObservableList<Guide> guideList = FXCollections.observableArrayList();
    private Guide selectedGuide = null;

    @FXML
    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        languagesColumn.setCellValueFactory(new PropertyValueFactory<>("languages"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));

        // Load guides
        refreshGuides();

        // Set up table selection listener
        guideTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedGuide = newSelection;
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });
    }

    private void refreshGuides() {
        guideList.setAll(GuideDAO.getAllGuides());
        guideTable.setItems(guideList);
    }

    private void populateForm(Guide guide) {
        nameField.setText(guide.getName());
        languagesField.setText(guide.getLanguages());
        experienceField.setText(String.valueOf(guide.getExperience()));
        contactField.setText(guide.getContact());
        emailField.setText(guide.getEmail());
        specializationField.setText(guide.getSpecialization());
        licenseField.setText(guide.getLicense());
        ratingSlider.setValue(guide.getRating()); // Set slider value
        availableCheckBox.setSelected(guide.isAvailable());
    }


    private void clearForm() {
        nameField.clear();
        languagesField.clear();
        experienceField.clear();
        contactField.clear();
        emailField.clear();
        specializationField.clear();
        licenseField.clear();
        ratingSlider.setValue(4.0); // Reset slider to default value
        availableCheckBox.setSelected(false);
        selectedGuide = null;
    }

    private boolean validateForm() {
        if (nameField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter guide name");
            return false;
        }
        if (languagesField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter languages");
            return false;
        }
        if (contactField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter contact number");
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
    private void handleAddGuide(ActionEvent event) {
        if (validateForm()) {
            try {
                Guide guide = new Guide(
                        0, // ID will be assigned by DAO
                        nameField.getText(),
                        languagesField.getText(),
                        Integer.parseInt(experienceField.getText()),
                        contactField.getText(),
                        emailField.getText(),
                        specializationField.getText(),
                        licenseField.getText(),
                        ratingSlider.getValue(), // Use slider value instead of text field
                        availableCheckBox.isSelected()
                );
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }

                if (GuideDAO.addGuide(guide)) {
                    refreshGuides();
                    clearForm();
                    showSuccessAlert("Guide added successfully!");
                } else {
                    showAlert("Error", "Failed to add guide");
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter valid numbers for experience");
            }
        }
    }

    @FXML
    private void handleUpdateGuide(ActionEvent event) {
        if (selectedGuide != null && validateForm()) {
            try {
                selectedGuide.setName(nameField.getText());
                selectedGuide.setLanguages(languagesField.getText());
                selectedGuide.setExperience(Integer.parseInt(experienceField.getText()));
                selectedGuide.setContact(contactField.getText());
                selectedGuide.setEmail(emailField.getText());
                selectedGuide.setSpecialization(specializationField.getText());
                selectedGuide.setLicense(licenseField.getText());
                selectedGuide.setRating(Double.parseDouble(String.valueOf(ratingSlider.getMax())));
                selectedGuide.setAvailable(availableCheckBox.isSelected());

                if (GuideDAO.updateGuide(selectedGuide)) {
                    refreshGuides();
                    clearForm();
                }
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter valid numbers for experience and rating");
            }
        }
    }

    @FXML
    private void handleDeleteGuide(ActionEvent event) {
        if (selectedGuide != null) {
            if (GuideDAO.deleteGuide(selectedGuide.getId())) {
                refreshGuides();
                clearForm();
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }
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
}