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
 * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
 */
public class ShowAlertUtils {

    /**
     * Displays an information alert dialog with the given title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the message content of the alert
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showInfo(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    /**
     * Displays a warning alert dialog with the given title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the message content of the alert
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showWarning(String title, String content) {
        showAlert(AlertType.WARNING, title, content);
    }

    /**
     * Displays an error alert dialog with the given title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the message content of the alert
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    /**
     * Displays an alert dialog with the specified alert type, title, and content.
     * This method is used internally by other helper methods.
     *
     * @param type    the type of alert (e.g., INFORMATION, WARNING, ERROR)
     * @param title   the title of the alert dialog
     * @param content the message content of the alert
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert dialog with the given title, header, and content,
     * and provides a consumer to handle the user's response (e.g., OK or Cancel).
     *
     * @param title   the title of the confirmation dialog
     * @param header  the header text displayed above the content
     * @param content the message content of the dialog
     * @param action  a {@link java.util.function.Consumer} that handles the button the user clicks
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showConfirmation(String title, String header, String content, java.util.function.Consumer<ButtonType> action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(action);
    }
}
