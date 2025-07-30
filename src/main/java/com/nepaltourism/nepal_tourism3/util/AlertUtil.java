package com.nepaltourism.nepal_tourism3.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Optional;

public class AlertUtil {

    // Basic alert with custom type, title and message
    public static void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        setupAlert(alert, title, message, null);
        alert.showAndWait();
    }

    // SafetyAlert with owner window specification
    public static void showAlert(AlertType type, String title, String message, Window owner) {
        Alert alert = new Alert(type);
        setupAlert(alert, title, message, owner);
        alert.showAndWait();
    }

    // Success notification
    public static void showSuccessAlert(String message) {
        showAlert(AlertType.INFORMATION, "Success", message);
    }

    // Error notification
    public static void showErrorAlert(String message, String s) {
        showAlert(AlertType.ERROR, "Error", message);
    }

    // Warning notification
    public static void showWarningAlert(String message) {
        showAlert(AlertType.WARNING, "Warning", message);
    }

    // Confirmation dialog
    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        setupAlert(alert, title, message, null);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Customizable dialog
    public static Optional<ButtonType> showCustomDialog(String title, String message,
                                                        AlertType type, ButtonType... buttons) {
        Alert alert = new Alert(type, "", buttons);
        setupAlert(alert, title, message, null);
        return alert.showAndWait();
    }

    // Fancy styled alert (for important notifications)
    public static void showStyledAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        setupAlert(alert, title, message, null);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    // Modal alert (blocks interaction with other windows)
    public static void showModalAlert(AlertType type, String title, String message, Window owner) {
        Alert alert = new Alert(type);
        setupAlert(alert, title, message, owner);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    // Common alert setup
    private static void setupAlert(Alert alert, String title, String message, Window owner) {
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (owner != null) {
            alert.initOwner(owner);
        }

        // You can add more default styling here if needed
    }

}