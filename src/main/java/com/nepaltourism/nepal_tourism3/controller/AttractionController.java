package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.AttractionDAO;
import com.nepaltourism.nepal_tourism3.model.Attraction;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import com.nepaltourism.nepal_tourism3.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AttractionController {
    // Table View components
    @FXML private TableView<Attraction> attractionTable;
    @FXML private TableColumn<Attraction, String> nameColumn;
    @FXML private TableColumn<Attraction, String> typeColumn;
    @FXML private TableColumn<Attraction, String> locationColumn;
    @FXML private TableColumn<Attraction, String> difficultyColumn;
    @FXML private TableColumn<Attraction, Number> altitudeColumn;
    @FXML private TableColumn<Attraction, Number> priceColumn;
    @FXML private TableColumn<Attraction, Number> durationColumn;

    // Form components
    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField locationField;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private TextField altitudeField;
    @FXML private TextField priceField;
    @FXML private TextField durationField;
    @FXML private TextField bestSeasonField;
    @FXML private CheckBox requiresGuideCheckBox;
    @FXML private TextArea descriptionArea;

    // Buttons
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @FXML
    public void initialize() {
        System.out.println("Initializing AttractionController...");

// Initialize columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        altitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

// Set up combo boxes
        typeComboBox.setEditable(true);
        typeComboBox.getItems().addAll("Trekking", "Cultural", "Adventure", "Religious", "Wildlife");

        difficultyComboBox.setEditable(true);
        difficultyComboBox.getItems().addAll("Easy", "Moderate", "Difficult", "Challenging");

// Delay table loading slightly until UI fully loaded
        javafx.application.Platform.runLater(() -> {
            refreshTable(); // <-- moved inside runLater
        });

        attractionTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFormWithAttraction(newSelection);
                        setFormMode(false);
                    } else {
                        clearForm();
                    }
                });

        setFormMode(true);

    }

    private void refreshTable() {
        System.out.println("=== Refreshing Table ===");
        List<Attraction> attractions = AttractionDAO.getAllAttractions();
        System.out.println("Found " + attractions.size() + " attractions in file");

        // Print all attractions for debugging
        attractions.forEach(a -> System.out.println(
                a.getId() + " | " +
                        a.getName() + " | " +
                        a.getLocation()
        ));

        attractionTable.setItems(FXCollections.observableArrayList(attractions));
        attractionTable.refresh();
    }


    private void populateFormWithAttraction(Attraction attraction) {
        System.out.println("Populating form with: " + attraction.getName());
        nameField.setText(attraction.getName());
        typeComboBox.setValue(attraction.getType());
        locationField.setText(attraction.getLocation());
        difficultyComboBox.setValue(attraction.getDifficulty());
        altitudeField.setText(String.valueOf(attraction.getAltitude()));
        priceField.setText(String.valueOf(attraction.getPrice()));
        durationField.setText(String.valueOf(attraction.getDuration()));
        bestSeasonField.setText(attraction.getBestSeason());
        requiresGuideCheckBox.setSelected(attraction.isRequiresGuide());
        descriptionArea.setText(attraction.getDescription());
    }

    private void setFormMode(boolean addMode) {
        addButton.setDisable(!addMode);
        updateButton.setDisable(addMode);
        deleteButton.setDisable(addMode);

        // Ensure all fields are enabled
        nameField.setDisable(false);
        typeComboBox.setDisable(false);
        locationField.setDisable(false);
        difficultyComboBox.setDisable(false);
        altitudeField.setDisable(false);
        priceField.setDisable(false);
        durationField.setDisable(false);
        bestSeasonField.setDisable(false);
        requiresGuideCheckBox.setDisable(false);
        descriptionArea.setDisable(false);

        // Ensure buttons are visible
        addButton.setVisible(true);
        updateButton.setVisible(true);
        deleteButton.setVisible(true);
        clearButton.setVisible(true);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setTitle("Guide Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to return to dashboard: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void handleAddAttraction(ActionEvent event) {
        try {
            // 1. Create the attraction
            Attraction attraction = new Attraction(
                    AttractionDAO.getNextId(),
                    nameField.getText(),
                    typeComboBox.getValue(),
                    locationField.getText(),
                    difficultyComboBox.getValue(),
                    Integer.parseInt(altitudeField.getText()),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(durationField.getText()),
                    bestSeasonField.getText(),
                    requiresGuideCheckBox.isSelected(),
                    descriptionArea.getText()

            );
            AdminDashboardController dashboardController = SessionManager.getDashboardController();
            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }
            // 2. Save to file
            AttractionDAO.addAttraction(attraction);

            // 3. Refresh ENTIRE table (simplest reliable way)
            refreshTable();

            // 4. Clear form
            clearForm();

            AlertUtil.showSuccessAlert("Attraction added successfully");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void handleUpdateAttraction(ActionEvent event) {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(nameField.getText());
                selected.setType(typeComboBox.getValue());
                selected.setLocation(locationField.getText());
                selected.setDifficulty(difficultyComboBox.getValue());
                selected.setAltitude(parseInteger(altitudeField.getText()));
                selected.setPrice(parseDouble(priceField.getText()));
                selected.setDuration(parseInteger(durationField.getText()));
                selected.setBestSeason(bestSeasonField.getText());
                selected.setRequiresGuide(requiresGuideCheckBox.isSelected());
                selected.setDescription(descriptionArea.getText());

                if (AttractionDAO.updateAttraction(selected)) {
                    AlertUtil.showSuccessAlert("Attraction updated successfully");
                    refreshTable();
                }
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }
            } catch (NumberFormatException e) {
                AlertUtil.showErrorAlert("Please enter valid numbers for altitude, price and duration", "Only administrators can access festival management");
            } catch (Exception e) {
                AlertUtil.showErrorAlert("Error updating attraction: " + e.getMessage(), "Only administrators can access festival management");
            }
        } else {
            AlertUtil.showWarningAlert("No attraction selected");
        }
    }

    @FXML
    private void handleDeleteAttraction(ActionEvent event) {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (AlertUtil.showConfirmationAlert("Confirm Delete",
                    "Are you sure you want to delete " + selected.getName() + "?")) {
                if (AttractionDAO.deleteAttraction(selected.getId())) {
                    AlertUtil.showSuccessAlert("Attraction deleted successfully");
                    refreshTable();
                    clearForm();
                    setFormMode(true);
                }
                AdminDashboardController dashboardController = SessionManager.getDashboardController();
                if (dashboardController != null) {
                    dashboardController.refreshDashboard();
                }
            }
        } else {
            AlertUtil.showWarningAlert("No attraction selected");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        nameField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        locationField.clear();
        difficultyComboBox.getSelectionModel().clearSelection();
        altitudeField.clear();
        priceField.clear();
        durationField.clear();
        bestSeasonField.clear();
        requiresGuideCheckBox.setSelected(false);
        descriptionArea.clear();
        attractionTable.getSelectionModel().clearSelection();
        setFormMode(true);
    }

    // Helper methods for number parsing
    private int parseInteger(String text) throws NumberFormatException {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    private double parseDouble(String text) throws NumberFormatException {
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    public void initializeViewAllMode() {
    }
}