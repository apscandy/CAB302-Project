package com.cab302.cab302project.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
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
}
