package com.nepaltourism.nepal_tourism3.controller;

import com.nepaltourism.nepal_tourism3.dao.UserDAO;
import com.nepaltourism.nepal_tourism3.model.User;
import com.nepaltourism.nepal_tourism3.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false); // Hide error by default
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in both username and password.");
            return;
        }

        Optional<User> userOptional = UserDAO.validateLogin(username, password);//here
        if (userOptional.isPresent()) {
            SessionManager.setCurrentUser(userOptional.get());
            loadDashboard();
        } else {
            showError("Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/Register.fxml"));
            Parent registerRoot = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
            stage.setTitle("Register - Nepal Tourism Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to open registration page.");
        }
    }

    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nepaltourism/fxml/fxml/AdminDashboard.fxml"));
            Parent dashboardRoot = loader.load();

            AdminDashboardController dashboardController = loader.getController();
            SessionManager.setDashboardController(dashboardController);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard - Nepal Tourism Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load dashboard.");
        }
    }


    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
