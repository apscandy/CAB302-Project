package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.EmailAlreadyInUseException;
import com.cab302.cab302project.error.model.question.FailedToGetQuestionsException;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.PasswordUtils;
import com.cab302.cab302project.util.RegexValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileController {


    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private TextField OldEmailAddressTextField;
    @FXML private TextField NewEmailAddressTextField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;


    private final SqliteUserDAO userDAO = new SqliteUserDAO();
    private final SqliteUserSecurityQuestionDAO secDAO = new SqliteUserSecurityQuestionDAO();

    @FXML
    private void handleUpdateEmail() {
        String oldEmail  = ApplicationState.getCurrentUser().getEmail();
        String oldEmailInput = OldEmailAddressTextField.getText().trim();
        String newEmail = NewEmailAddressTextField.getText().trim();

        if (oldEmail.isEmpty() || newEmail.isEmpty()) {
            showAlert("Validation Error", "Please fill in both email fields.");
            return;
        }

        if (!oldEmail.equals(oldEmailInput)) {
            showAlert("Validation Error", "Old email does not match your current email.");
            return;
        }

        if (oldEmail.equals(newEmail)) {
            showAlert("Validation Error", "New email must be different from the current one.");
            return;
        }

        if (!RegexValidator.validEmailAddress(newEmail)) {
            showAlert("Validation Error", "Invalid email format.");
            return;
        }

        try {
            new AuthenticationService().emailCheck(newEmail);
        } catch (EmailAlreadyInUseException e) {
            showAlert("Validation Error", "Email address already in use");
            return;
        }

        try {
            ApplicationState.getCurrentUser().setEmail(newEmail);
            userDAO.updateUser(ApplicationState.getCurrentUser());
            showAlert("Success", "Email updated successfully.");
            ApplicationState.logout();

            Stage stage = (Stage) OldEmailAddressTextField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showAlert("Error", "Failed to update email: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelUpdateEmail() {
        try {
            Stage stage = (Stage) OldEmailAddressTextField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to cancel: " + e.getMessage());
        }
    }


    @FXML
    private void handleUpdatePassword() {
        String oldPassword  = ApplicationState.getCurrentUser().getPassword();
        String oldPasswordInput = oldPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String oldPasswordInputHash = PasswordUtils.hashSHA256(oldPasswordInput);

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            showAlert("Validation Error", "Please fill in both password fields.");
            return;
        }

        if (!oldPassword.equals(oldPasswordInputHash)) {
            showAlert("Validation Error", "Old password does not match your current password.");
            return;
        }

        if (oldPasswordInput.equals(newPassword)) {
            showAlert("Validation Error", "New password must be different from the current one.");
            return;
        }

        if (!RegexValidator.validPassword(newPassword)) {
            showAlert("Validation Error", "Password must be at least 8 characters, include 1 number and 1 special character.");
            return;
        }

        try {
            ApplicationState.getCurrentUser().setPassword(PasswordUtils.hashSHA256(newPassword));
            userDAO.updateUser(ApplicationState.getCurrentUser());
            showAlert("Success", "Password updated successfully.");
            ApplicationState.logout();

            Stage stage = (Stage) oldPasswordField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to cancel: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelUpdatePassword() {
        try {
            Stage stage = (Stage) oldPasswordField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to cancel: " + e.getMessage());
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
