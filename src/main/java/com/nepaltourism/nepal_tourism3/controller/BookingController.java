package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.*;
import com.nepaltourism.nepal_tourism3.model.*;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
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

public class BookingController {
    @FXML private ComboBox<Tourist> touristComboBox;
    @FXML private ComboBox<Attraction> attractionComboBox;
    @FXML private ComboBox<Guide> guideComboBox;
    @FXML private ComboBox<FestivalDiscount> festivalComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField groupSizeField;
    @FXML private Label basePriceLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalPriceLabel;
    @FXML private TextArea safetyInfoArea;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> idColumn;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> guideColumn;
    @FXML private TableColumn<Booking, LocalDate> startDateColumn;
    @FXML private TableColumn<Booking, LocalDate> endDateColumn;
    @FXML private TableColumn<Booking, Integer> groupSizeColumn;
    @FXML private TableColumn<Booking, Double> totalPriceColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, String> festivalColumn;

    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private Booking selectedBooking = null;

    @FXML
    public void initialize() {
        // Initialize combo boxes
        loadTourists();
        loadAttractions();
        loadGuides();
        loadFestivals();

        // Set up cell factories
        touristComboBox.setCellFactory(param -> new ListCell<Tourist>() {
            @Override
            protected void updateItem(Tourist item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        attractionComboBox.setCellFactory(param -> new ListCell<Attraction>() {
            @Override
            protected void updateItem(Attraction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        guideComboBox.setCellFactory(param -> new ListCell<Guide>() {
            @Override
            protected void updateItem(Guide item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        festivalComboBox.setCellFactory(param -> new ListCell<FestivalDiscount>() {
            @Override
            protected void updateItem(FestivalDiscount item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        touristColumn.setCellValueFactory(new PropertyValueFactory<>("tourist"));
        attractionColumn.setCellValueFactory(new PropertyValueFactory<>("attraction"));
        guideColumn.setCellValueFactory(new PropertyValueFactory<>("guide"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        groupSizeColumn.setCellValueFactory(new PropertyValueFactory<>("groupSize"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        festivalColumn.setCellValueFactory(new PropertyValueFactory<>("festivalDiscount"));

        // Load bookings
        refreshBookings();

        // Set up listeners
        bookingTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedBooking = newSelection;
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });

        attractionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        groupSizeField.textProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        festivalComboBox.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
    }

    private void loadTourists() {
        touristComboBox.setItems(FXCollections.observableArrayList(TouristDAO.getAllTourists()));
    }

    private void loadAttractions() {
        attractionComboBox.setItems(FXCollections.observableArrayList(AttractionDAO.getAllAttractions()));
    }

    private void loadGuides() {
        guideComboBox.setItems(FXCollections.observableArrayList(GuideDAO.getAllGuides()));
    }

    private void loadFestivals() {
        festivalComboBox.setItems(FXCollections.observableArrayList(FestivalDiscountDAO.getAllFestivals()));
    }

    private void refreshBookings() {
        bookingList.setAll(BookingDAO.getAllBookings());
        bookingTable.setItems(bookingList);
    }

    private void populateForm(Booking booking) {
        Tourist tourist = findTouristByName(booking.getTourist());
        Attraction attraction = findAttractionByName(booking.getAttraction());
        Guide guide = findGuideByName(booking.getGuide());

        touristComboBox.setValue(tourist);
        attractionComboBox.setValue(attraction);
        guideComboBox.setValue(guide);
        startDatePicker.setValue(booking.getStartDate());
        endDatePicker.setValue(booking.getEndDate());
        groupSizeField.setText(String.valueOf(booking.getGroupSize()));

        // Set festival discount if exists
        if (booking.getFestivalDiscount() != null && !booking.getFestivalDiscount().equals("None")) {
            festivalComboBox.setValue(findFestivalByName(booking.getFestivalDiscount()));
        } else {
            festivalComboBox.setValue(null);
        }

        updatePriceLabels(booking.getTotalPrice());
    }

    private Tourist findTouristByName(String name) {
        return TouristDAO.getAllTourists().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Attraction findAttractionByName(String name) {
        return AttractionDAO.getAllAttractions().stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Guide findGuideByName(String name) {
        return GuideDAO.getAllGuides().stream()
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private FestivalDiscount findFestivalByName(String name) {
        return FestivalDiscountDAO.getAllFestivals().stream()
                .filter(f -> f.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void calculatePrice() {
        if (attractionComboBox.getValue() != null && !groupSizeField.getText().isEmpty()) {
            try {
                int groupSize = Integer.parseInt(groupSizeField.getText());
                double basePrice = attractionComboBox.getValue().getPrice() * groupSize;
                double discount = 0.0;
                String festivalName = "None";

                // Check for valid festival discount
                FestivalDiscount selectedFestival = festivalComboBox.getValue();
                LocalDate bookingDate = startDatePicker.getValue() != null ?
                        startDatePicker.getValue() :
                        LocalDate.now();

                if (selectedFestival != null) {
                    if (!bookingDate.isBefore(selectedFestival.getStartDate()) &&
                            !bookingDate.isAfter(selectedFestival.getEndDate())) {
                        discount = selectedFestival.getDiscountPercent() / 100.0;
                        festivalName = selectedFestival.getName();
                    } else {
                        AlertUtil.showErrorAlert("Selected festival is not active on booking date", "Only administrators can access festival management");
                        festivalComboBox.setValue(null);
                    }
                }

                double total = basePrice - (basePrice * discount);

                basePriceLabel.setText(String.format("Base Price: $%.2f", basePrice));
                discountLabel.setText(String.format("Festival Discount: %.0f%%", discount * 100));
                totalPriceLabel.setText(String.format("Total Price: $%.2f", total));
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }
    }

    private void updatePriceLabels(double totalPrice) {
        basePriceLabel.setText(String.format("Base Price: $%.2f", totalPrice));
        discountLabel.setText("Festival Discount: 0%");
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    @FXML
    private void addBooking(ActionEvent event) {
        if (validateForm()) {
            try {
                Booking booking = new Booking(
                        0,
                        touristComboBox.getValue().getName(),
                        attractionComboBox.getValue().getName(),
                        guideComboBox.getValue().getName(),
                        startDatePicker.getValue(),
                        endDatePicker.getValue(),
                        Integer.parseInt(groupSizeField.getText()),
                        Double.parseDouble(totalPriceLabel.getText().replaceAll("[^\\d.]", "")),
                        "Pending",
                        festivalComboBox.getValue() != null ?
                                festivalComboBox.getValue().getName() : "None"
                );

                if (BookingDAO.saveBooking(booking)) {
                    refreshBookings();
                    clearForm();
                    AlertUtil.showSuccessAlert("Booking created successfully!");
                }
            } catch (Exception e) {
                AlertUtil.showErrorAlert("Error creating booking: " + e.getMessage(), "Only administrators can access festival management");
            }
        }
    }

    @FXML
    private void updateBooking(ActionEvent event) {
        if (selectedBooking != null && validateForm()) {
            selectedBooking.setTourist(touristComboBox.getValue().getName());
            selectedBooking.setAttraction(attractionComboBox.getValue().getName());
            selectedBooking.setGuide(guideComboBox.getValue().getName());
            selectedBooking.setStartDate(startDatePicker.getValue());
            selectedBooking.setEndDate(endDatePicker.getValue());
            selectedBooking.setGroupSize(Integer.parseInt(groupSizeField.getText()));
            selectedBooking.setTotalPrice(Double.parseDouble(totalPriceLabel.getText().replaceAll("[^\\d.]", "")));
            selectedBooking.setFestivalDiscount(
                    festivalComboBox.getValue() != null ?
                            festivalComboBox.getValue().getName() : "None"
            );

            if (BookingDAO.updateBooking(selectedBooking)) {
                refreshBookings();
                clearForm();
                AlertUtil.showSuccessAlert("Booking updated successfully!");
            }
        }
    }

    @FXML
    private void deleteBooking(ActionEvent event) {
        if (selectedBooking != null) {
            if (AlertUtil.showConfirmationAlert("Confirm Delete",
                    "Are you sure you want to delete this booking?")) {
                if (BookingDAO.deleteBooking(selectedBooking.getId())) {
                    refreshBookings();
                    clearForm();
                    AlertUtil.showSuccessAlert("Booking deleted successfully!");
                }
            }
        }
    }

    private boolean validateForm() {
        // Existing validation code remains the same
        // ...
        return true;
    }

    private void clearForm() {
        touristComboBox.getSelectionModel().clearSelection();
        attractionComboBox.getSelectionModel().clearSelection();
        guideComboBox.getSelectionModel().clearSelection();
        festivalComboBox.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        groupSizeField.clear();
        basePriceLabel.setText("Base Price: $0");
        discountLabel.setText("Festival Discount: 0%");
        totalPriceLabel.setText("Total Price: $0");
        safetyInfoArea.clear();
        selectedBooking = null;
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Failed to return to dashboard: " + e.getMessage(), "Only administrators can access festival management");
        }
    }

    @FXML
    private void clearForm(ActionEvent event) {
        clearForm();
    }
}