package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.UserDAO;
import com.nepaltourism.nepal_tourism3.model.User;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    // All your FXML injections remain the same
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private RadioButton touristRadio;
    @FXML private RadioButton guideRadio;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;
    @FXML private TextField touristNameField;
    @FXML private TextField touristContactField;
    @FXML private TextField emergencyContactField;
    @FXML private TextField emergencyNameField;
    @FXML private TextField passportField;
    @FXML private ComboBox<String> nationalityComboBox;
    @FXML private TextField guideNameField;
    @FXML private TextField guideContactField;

    @FXML
    public void initialize() {
        ToggleGroup roleGroup = new ToggleGroup();
        touristRadio.setToggleGroup(roleGroup);
        guideRadio.setToggleGroup(roleGroup);
        touristRadio.setSelected(true);

        // Initialize nationality combobox
        nationalityComboBox.getItems().addAll(
                "Nepali", "Indian", "Chinese", "American", "British",
                "Australian", "Japanese", "Korean", "French", "German"
        );
        nationalityComboBox.getSelectionModel().selectFirst();

        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        // Clear previous messages
        errorLabel.setVisible(false);
        successLabel.setVisible(false);

        // Get basic fields
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = touristRadio.isSelected() ? "tourist" : "guide";

        // Validate basic fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        // Get profile info based on role
        String fullName, contactNumber, emergencyContact, nationality, passportNumber, emergencyContactName;

        if (touristRadio.isSelected()) {
            fullName = touristNameField.getText().trim();
            contactNumber = touristContactField.getText().trim();
            emergencyContact = emergencyContactField.getText().trim();
            nationality = nationalityComboBox.getValue();
            passportNumber = passportField.getText().trim();
            emergencyContactName = emergencyNameField.getText().trim();

            if (fullName.isEmpty() || contactNumber.isEmpty() || emergencyContact.isEmpty()) {
                showError("Please fill all required tourist fields.");
                return;
            }
        } else {
            fullName = guideNameField.getText().trim();
            contactNumber = guideContactField.getText().trim();

            if (fullName.isEmpty() || contactNumber.isEmpty()) {
                showError("Please fill all required guide fields.");
                return;
            }

            // Default values for guide
            emergencyContact = "0000000000";
            nationality = "Nepali";
            passportNumber = "";
            emergencyContactName = fullName;
        }

        // Create and register user
        User newUser = new User(username, email, password, role,
                fullName, contactNumber, emergencyContact,
                nationality, passportNumber, emergencyContactName);

        String result = UserDAO.registerUser(newUser);
        if (result == null) {
            successLabel.setText("Registration successful! You can now log in.");
            successLabel.setVisible(true);
        } else {
            showError(result);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void backToLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to open login screen.");
        }
    }
}