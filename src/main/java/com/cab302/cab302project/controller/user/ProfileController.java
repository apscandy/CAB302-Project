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
import com.cab302.cab302project.util.ShowAlertUtils;
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

/**
 * Controller class for handling user profile-related actions, such as updating email and password.
 * This class is responsible for validating user inputs, updating data through DAOs,
 * and navigating between views using JavaFX.
 *
 * <p>It handles the following actions:
 * <ul>
 *     <li>Update email address with validation and duplication check</li>
 *     <li>Update password with format and confirmation validation</li>
 *     <li>Cancel email or password update and return to main view</li>
 * </ul>
 * </p>
 * @author Dang Linh Phan - Lewis (n11781840)
 */
public class ProfileController {

    // FXML components for email and password update forms
    @FXML private PasswordField newPasswordFieldConfirm;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private TextField OldEmailAddressTextField;
    @FXML private TextField NewEmailAddressTextField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;

    // DAO objects to interact with the database
    private final SqliteUserDAO userDAO = new SqliteUserDAO();
    private final SqliteUserSecurityQuestionDAO secDAO = new SqliteUserSecurityQuestionDAO();

    /**
     * Handles the update of the user's email address.
     * Validates input fields, checks if the new email is in correct format and not already in use.
     * Updates the user's email in the database and redirects to the login screen on success.
     */
    @FXML
    private void handleUpdateEmail() {
        String oldEmail  = ApplicationState.getCurrentUser().getEmail();
        String oldEmailInput = OldEmailAddressTextField.getText().trim();
        String newEmail = NewEmailAddressTextField.getText().trim();

        if (oldEmail.isEmpty() || newEmail.isEmpty()) {
            ShowAlertUtils.showWarning("Validation Error", "Please fill in both email fields.");
            return;
        }

        if (!oldEmail.equals(oldEmailInput)) {
            ShowAlertUtils.showWarning("Validation Error", "Old email does not match your current email.");
            return;
        }

        if (oldEmail.equals(newEmail)) {
            ShowAlertUtils.showWarning("Validation Error", "New email must be different from the current one.");
            return;
        }

        if (!RegexValidator.validEmailAddress(newEmail)) {
            ShowAlertUtils.showWarning("Validation Error", "Invalid email format.");
            return;
        }

        try {
            new AuthenticationService().emailCheck(newEmail);
        } catch (EmailAlreadyInUseException e) {
            ShowAlertUtils.showWarning("Validation Error", "Email address already in use");
            return;
        }

        try {
            ApplicationState.getCurrentUser().setEmail(newEmail);
            userDAO.updateUser(ApplicationState.getCurrentUser());
            ShowAlertUtils.showInfo("Success", "Email updated successfully.");
            ApplicationState.logout();

            Stage stage = (Stage) OldEmailAddressTextField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            ShowAlertUtils.showError("Error", "Failed to update email: " + e.getMessage());
        }
    }

    /**
     * Cancels the email update process and redirects the user to the main view.
     */
    @FXML
    private void handleCancelUpdateEmail() {
        try {
            Stage stage = (Stage) OldEmailAddressTextField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ShowAlertUtils.showError("Error", "Failed to cancel: " + e.getMessage());
        }
    }

    /**
     * Handles the update of the user's password.
     * Validates input fields, checks password strength and confirmation match.
     * Updates the user's password in the database and redirects to the main view on success.
     */
    @FXML
    private void handleUpdatePassword() {
        String oldPassword = ApplicationState.getCurrentUser().getPassword();
        String oldPasswordInput = oldPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = newPasswordFieldConfirm.getText().trim();
        String oldPasswordInputHash = PasswordUtils.hashSHA256(oldPasswordInput);

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            ShowAlertUtils.showWarning("Validation Error", "Please fill in both password fields.");
            return;
        }

        if (!oldPassword.equals(oldPasswordInputHash)) {
            ShowAlertUtils.showWarning("Validation Error", "Old password does not match your current password.");
            return;
        }

        if (oldPasswordInput.equals(newPassword)) {
            ShowAlertUtils.showWarning("Validation Error", "New password must be different from the current one.");
            return;
        }

        if (!RegexValidator.validPassword(newPassword)) {
            ShowAlertUtils.showWarning("Validation Error", "Password must be at least 8 characters, include 1 number and 1 special character.");
            return;
        }

        if (!confirmPassword.equals(newPassword)) {
            ShowAlertUtils.showWarning("Validation Error", "confirm password not match.");
            return;
        }

        try {
            ApplicationState.getCurrentUser().setPassword(PasswordUtils.hashSHA256(newPassword));
            userDAO.updateUser(ApplicationState.getCurrentUser());
            ShowAlertUtils.showInfo("Success", "Password updated successfully.");

            Stage stage = (Stage) oldPasswordField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ShowAlertUtils.showError("Error", "Failed to cancel: " + e.getMessage());
        }
    }

    /**
     * Cancels the password update process and redirects the user to the main view.
     */
    @FXML
    private void handleCancelUpdatePassword() {
        try {
            Stage stage = (Stage) oldPasswordField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ShowAlertUtils.showError("Error", "Failed to cancel: " + e.getMessage());
        }
    }

}
