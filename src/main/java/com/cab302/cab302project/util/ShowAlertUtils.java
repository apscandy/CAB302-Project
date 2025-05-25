package com.cab302.cab302project.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Utility class for displaying various types of JavaFX alert dialogs.
 * <p>
 * Provides static helper methods to show:
 * <ul>
 *     <li><strong>Information</strong> alerts</li>
 *     <li><strong>Warning</strong> alerts</li>
 *     <li><strong>Error</strong> alerts</li>
 *     <li><strong>Confirmation</strong> alerts with response handling</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:</p>
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
     * Displays an information alert dialog.
     * <p>
     * The alert contains the specified title and message content.
     * </p>
     *
     * @param title   the title of the alert dialog
     * @param content the message content to display in the dialog
     * @see javafx.scene.control.Alert.AlertType#INFORMATION
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showInfo(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    /**
     * Displays a warning alert dialog.
     * <p>
     * The alert contains the specified title and message content.
     * </p>
     *
     * @param title   the title of the alert dialog
     * @param content the message content to display in the dialog
     * @see javafx.scene.control.Alert.AlertType#WARNING
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showWarning(String title, String content) {
        showAlert(AlertType.WARNING, title, content);
    }

    /**
     * Displays an error alert dialog.
     * <p>
     * The alert contains the specified title and message content.
     * </p>
     *
     * @param title   the title of the alert dialog
     * @param content the message content to display in the dialog
     * @see javafx.scene.control.Alert.AlertType#ERROR
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public static void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    /**
     * Displays an alert dialog of the specified type.
     * <p>
     * This internal method is used by the public helper methods to reduce redundancy.
     * </p>
     *
     * @param type    the type of alert (e.g., INFORMATION, WARNING, ERROR)
     * @param title   the title of the alert dialog
     * @param content the message content to display in the dialog
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
     * Displays a confirmation alert dialog with the specified title, header, and content.
     * <p>
     * The user's response (e.g., OK or Cancel) is passed to the given action consumer.
     * </p>
     *
     * @param title   the title of the confirmation dialog
     * @param header  the header text displayed above the content
     * @param content the message content of the dialog
     * @param action  a {@link java.util.function.Consumer} to handle the user's button selection
     * @see javafx.scene.control.Alert.AlertType#CONFIRMATION
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
