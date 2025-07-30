package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.FestivalDiscountDAO;
import com.nepaltourism.nepal_tourism3.model.FestivalDiscount;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
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
import java.time.LocalDate;
import java.util.List;

public class FestivalManagementController {
    // Table View components
    @FXML private TableView<FestivalDiscount> festivalTable;
    @FXML private TableColumn<FestivalDiscount, String> nameColumn;
    @FXML private TableColumn<FestivalDiscount, LocalDate> startDateColumn;
    @FXML private TableColumn<FestivalDiscount, LocalDate> endDateColumn;
    @FXML private TableColumn<FestivalDiscount, Double> discountColumn;

    // Form components
    @FXML private TextField nameField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField discountField;

    // Buttons
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @FXML
    public void initialize() {
        // Initialize columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));

        // Load data
        refreshTable();

        // Set up table selection listener
        festivalTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFormWithFestival(newSelection);
                        setFormMode(false);
                    } else {
                        clearForm();
                    }
                });

        setFormMode(true);
    }

    private void refreshTable() {
        List<FestivalDiscount> festivals = FestivalDiscountDAO.getAllFestivals();
        festivalTable.setItems(FXCollections.observableArrayList(festivals));
        festivalTable.refresh();
    }

    private void populateFormWithFestival(FestivalDiscount festival) {
        nameField.setText(festival.getName());
        startDatePicker.setValue(festival.getStartDate());
        endDatePicker.setValue(festival.getEndDate());
        discountField.setText(String.valueOf(festival.getDiscountPercent()));
    }

    private void setFormMode(boolean addMode) {
        addButton.setDisable(!addMode);
        updateButton.setDisable(addMode);
        deleteButton.setDisable(addMode);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    @FXML
    private void handleAddFestival(ActionEvent event) {
        try {
            FestivalDiscount festival = new FestivalDiscount(
                    FestivalDiscountDAO.getNextId(),
                    nameField.getText(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    Double.parseDouble(discountField.getText())

            );

            if (FestivalDiscountDAO.addFestival(festival)) {
                AlertUtil.showSuccessAlert("Festival discount added successfully");
                refreshTable();
                clearForm();
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error adding festival: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void handleUpdateFestival(ActionEvent event) {
        FestivalDiscount selected = festivalTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(nameField.getText());
                selected.setStartDate(startDatePicker.getValue());
                selected.setEndDate(endDatePicker.getValue());
                selected.setDiscountPercent(Double.parseDouble(discountField.getText()));

                if (FestivalDiscountDAO.updateFestival(selected)) {
                    AlertUtil.showSuccessAlert("Festival updated successfully");
                    refreshTable();
                }
            } catch (Exception e) {
                AlertUtil.showErrorAlert("Error updating festival: " + e.getMessage(), "Only administrators can access festival management");
            }
        } else {
            AlertUtil.showWarningAlert("No festival selected");
        }
    }

    @FXML
    private void handleDeleteFestival(ActionEvent event) {
        FestivalDiscount selected = festivalTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (AlertUtil.showConfirmationAlert("Confirm Delete",
                    "Are you sure you want to delete " + selected.getName() + "?")) {
                if (FestivalDiscountDAO.deleteFestival(selected.getId())) {
                    AlertUtil.showSuccessAlert("Festival deleted successfully");
                    refreshTable();
                    clearForm();
                    setFormMode(true);
                }
            }
        } else {
            AlertUtil.showWarningAlert("No festival selected");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        nameField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        discountField.clear();
        festivalTable.getSelectionModel().clearSelection();
        setFormMode(true);
    }
}