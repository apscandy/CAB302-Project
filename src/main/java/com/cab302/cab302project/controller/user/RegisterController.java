package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.userSecQuestions.IUserSecurityQuestionDAO;
import com.cab302.cab302project.util.RegexValidator;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.User;

import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code RegisterController} class provides methods to handle
 * button function in FXML file, checking validated data, add those data to tempUser and move to security question window.
 * This class link to register-view.fxml to control the Ul for register.
 *
 * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
 */
public class RegisterController {
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private TextField LastNameTextField;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private PasswordField SetPasswordField;
    @FXML
    private PasswordField ConfirmPasswordField;
    @FXML
    private Button NextButton;
    @FXML
    private Button BackButton;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String confirmPassword;

    /**
     * Navigates back to the prompt-email view when "Back" button is clicked.
     */
    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Validates input fields and, if successful, transitions to the security question view.
     */
    public void NextButtonAction() throws IOException {
        if (registerUser()) {
            // Load the next view (add security questions)
            User user = new User(firstName, lastName, email, password);
            Stage stage = (Stage) NextButton.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/register/add-questions-security-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            // Pass temporary user to the next controller
            AddSecurityQuestionController addSecurityQuestionController = fxmlLoader.getController();
            addSecurityQuestionController.setTempUser(user);
        }
    }

    /**
     * <p>
     * Validates all user input fields. If valid, creates a User object and stores
     * it temporarily for use in the Add Security Question step.
     * </p>
     * @return true if all fields are valid, false otherwise
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
     */
    public boolean registerUser() {

        // get the user information.
        firstName = FirstNameTextField.getText();
        lastName = LastNameTextField.getText();
        email = EmailAddressTextField.getText();
        password = SetPasswordField.getText();
        confirmPassword = ConfirmPasswordField.getText();

        // validate first name
        if (firstName.isEmpty()) {
            ShowAlertUtils.showError("Registration Error", "First name cannot be empty.");
            return false;
        }

        // validate last name
        if (lastName.isEmpty()) {
            ShowAlertUtils.showError("Registration Error", "Last name cannot be empty.");
            return false;
        }

        // validate email
        if (email.isEmpty()) {
            ShowAlertUtils.showError("Registration Error", "Email cannot be empty.");
            return false;
        }
        if (!RegexValidator.validEmailAddress(email)) {
            ShowAlertUtils.showError("Registration Error", "Invalid email format.");
            return false;
        }
        // Check if email already exists in database
        try {
            new AuthenticationService().emailCheck(email);
        } catch (EmailAlreadyInUseException e) {
            ShowAlertUtils.showError("Registration Error", "Email address already in use");
            return false;
        }

        // validate password
        if (password.isEmpty()) {
            ShowAlertUtils.showError("Registration Error", "Password cannot be empty.");
            return false;
        }
        if (!RegexValidator.validPassword(password)) {
            ShowAlertUtils.showError("Registration Error", "Password must be at least 8 characters, include 1 number and 1 special character.");
            return false;
        }
        // Confirm passwords match
        else if (!password.equals(confirmPassword)) {
            ShowAlertUtils.showError("Registration Error", "Passwords do not match.");
            return false;
        }
        return true;
    }

}
