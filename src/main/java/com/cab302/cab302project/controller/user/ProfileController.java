package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.EmailAlreadyInUseException;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.util.PasswordUtils;
import com.cab302.cab302project.util.RegexValidator;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for managing user profile actions in the application.
 * <p>
 * This class is responsible for handling user interactions related to:
 * <ul>
 *     <li>Updating the email address (with validation and duplication check)</li>
 *     <li>Updating the password (with format and confirmation validation)</li>
 *     <li>Cancelling updates and navigating back to the main view</li>
 * </ul>
 * </p>
 *
 * <p>It uses JavaFX FXML annotations to bind UI components and provides database interaction through DAO classes.</p>
 *
 * @see com.cab302.cab302project.model.user.SqliteUserDAO
 * @see com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO
 * @see com.cab302.cab302project.util.ShowAlertUtils
 * @see com.cab302.cab302project.util.PasswordUtils
 * @see com.cab302.cab302project.util.RegexValidator
 *
 * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
 */

public class ProfileController {

    // FXML components for email and password update forms
    @FXML private PasswordField newPasswordFieldConfirm;
    @FXML private TextField OldEmailAddressTextField;
    @FXML private TextField NewEmailAddressTextField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;

    // DAO objects to interact with the database
    private final SqliteUserDAO userDAO = new SqliteUserDAO();
    private final SqliteUserSecurityQuestionDAO secDAO = new SqliteUserSecurityQuestionDAO();

    /**
     * Handles the process of updating the user's email address.
     * <p>
     * This method performs the following checks:
     * <ul>
     *     <li>Old and new email fields must be filled</li>
     *     <li>Old email must match the currently logged-in user's email</li>
     *     <li>New email must be different from the old one</li>
     *     <li>New email must match a valid email format</li>
     *     <li>New email must not be already registered in the system</li>
     * </ul>
     * </p>
     * If validation passes, the user's email is updated in the database and the user is logged out and redirected to the login screen.
     *
     * @see com.cab302.cab302project.error.authentication.EmailAlreadyInUseException
     * @see com.cab302.cab302project.util.RegexValidator#validEmailAddress(String)
     * @see com.cab302.cab302project.util.ShowAlertUtils
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
     * Cancels the email update process and redirects the user back to the main view.
     * <p>
     * This action discards all unsaved changes and loads the "main.fxml" scene.
     * </p>
     *
     * @see javafx.stage.Stage
     * @see javafx.fxml.FXMLLoader
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
     * Handles the process of updating the user's password.
     * <p>
     * This method performs the following validations:
     * <ul>
     *     <li>Old and new password fields must be filled</li>
     *     <li>Old password must match the currently logged-in user's password (hashed)</li>
     *     <li>New password must be different from the old one</li>
     *     <li>New password must satisfy strength criteria (minimum 8 characters, 1 number, 1 special character)</li>
     *     <li>Confirmation password must match the new password</li>
     * </ul>
     * </p>
     * On success, the password is hashed, updated in the database, and the user is returned to the main view.
     *
     * @see com.cab302.cab302project.util.PasswordUtils
     * @see com.cab302.cab302project.util.RegexValidator#validPassword(String)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
     * Cancels the password update process and redirects the user back to the main view.
     * <p>
     * This action discards all unsaved changes and loads the "main.fxml" scene.
     * </p>
     *
     * @see javafx.stage.Stage
     * @see javafx.fxml.FXMLLoader
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
