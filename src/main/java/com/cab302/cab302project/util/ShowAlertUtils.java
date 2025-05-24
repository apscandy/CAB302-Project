package com.cab302.cab302project.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Utility class for displaying different types of alert dialogs in JavaFX.
 * Provides static helper methods to show informational, warning, error, and confirmation alerts.
 * Example usage:
 * <pre>{@code
 * ShowAlertUtils.showInfo("Success", "Operation completed successfully.");
 * ShowAlertUtils.showError("Error", "Something went wrong.");
 * ShowAlertUtils.showConfirmation("Confirm", "Are you sure?", "This action is irreversible.", response -> {
 *     if (response == ButtonType.OK) {
 *         // proceed with action
 *     }
 * });
 * }</pre>
 *
 * @author Dang Linh Phan - Lewis (n11781840)
 */
public class ShowAlertUtils {

    public static void showInfo(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    public static void showWarning(String title, String content) {
        showAlert(AlertType.WARNING, title, content);
    }

    public static void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showConfirmation(String title, String header, String content, java.util.function.Consumer<ButtonType> action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(action);
    }
}
