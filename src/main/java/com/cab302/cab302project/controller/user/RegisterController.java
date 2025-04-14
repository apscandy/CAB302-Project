package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.userSecQuestions.IUserSecurityQuestionDAO;
import com.cab302.cab302project.util.RegexValidator;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.User;

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
 * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
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

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-questions-security-view.fxml"));

            // Pass temporary user to the next controller
            AddSecurityQuestionController addSecurityQuestionController = fxmlLoader.getController();
            addSecurityQuestionController.setTempUser(user);

            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
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
            setError(FirstNameTextField, "First name cannot be empty.");
            return false;
        }

        // validate last name
        if (lastName.isEmpty()) {
            setError(LastNameTextField, "Last name cannot be empty.");
            return false;
        }

        // validate email
        if (email.isEmpty()) {
            setError(EmailAddressTextField, "Email cannot be empty.");
            return false;
        }
        if (!RegexValidator.validEmailAddress(email)) {
            setError(EmailAddressTextField, "Invalid email format.");
            return false;
        }
        // Check if email already exists in database
        try {
            new AuthenticationService().emailCheck(email);
        } catch (EmailAlreadyInUseException e) {
            setError(EmailAddressTextField, "Email address already in use");
            return false;
        }

        // validate password
        if (password.isEmpty()) {
            setError(SetPasswordField, "Password cannot be empty.");
            return false;
        }
        if (!RegexValidator.validPassword(password)) {
            setError(SetPasswordField, "Password must be at least 8 characters, include 1 number and 1 special character.");
            return false;
        }
        // Confirm passwords match
        else if (!password.equals(confirmPassword)) {
            setError(ConfirmPasswordField, "Passwords do not match.");
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Displays an error alert associated with a given input control.
     * </p>
     * @param input The TextInputControl that caused the error
     * @param message The message to display in the alert
     */
    private void setError(TextInputControl input, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
