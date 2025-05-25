package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.util.RegexValidator;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.User;

import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code RegisterController} class handles user input for the registration form,
 * validates all fields including email and password, and manages navigation
 * between the registration and security question views.
 * <p>This controller is bound to the {@code register-view.fxml} layout and coordinates
 * data collection for creating a temporary {@link com.cab302.cab302project.model.user.User} object.
 * Upon successful validation, the controller transitions to the Add Security Question stage
 * handled by {@link com.cab302.cab302project.controller.user.AddSecurityQuestionController}.
 * </p>
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Handling back and next button navigation events</li>
 *   <li>Validating input fields (first name, last name, email, password)</li>
 *   <li>Checking email uniqueness using {@link com.cab302.cab302project.error.authentication}</li>
 *   <li>Displaying validation feedback using {@link com.cab302.cab302project.util.ShowAlertUtils}</li>
 * </ul>
 * </p>
 * @see com.cab302.cab302project.model.user.User
 * @see com.cab302.cab302project.controller.user.AddSecurityQuestionController
 * @see com.cab302.cab302project.error.authentication
 * @see com.cab302.cab302project.util.RegexValidator
 * @see com.cab302.cab302project.util.ShowAlertUtils
 * @see <a href="../../../../user/register/register-view.fxml">register-view.fxml</a>
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
     * Navigates back to the prompt-email view when the "Back" button is clicked.
     * <p>
     * This method is triggered by the Back button and loads the {@code prompt-email-view.fxml} UI.
     * It allows the user to return to the previous login stage if they wish to cancel registration.
     * </p>
     * @throws IOException if the FXML file fails to load
     * @see com.cab302.cab302project.HelloApplication
     * @see <a href="../../../../user/login/prompt-email-view.fxml">prompt-email-view.fxml</a>
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Validates input fields and transitions to the "Add Security Questions" view if all inputs are valid.
     * <p>
     * This method checks that all registration fields are correct using {@link #registerUser()},
     * then creates a temporary {@link User} object and passes it to the next controller
     * {@link AddSecurityQuestionController} to complete the second step of registration.
     * </p>
     *
     * @throws IOException if the FXML file for the next view fails to load
     * @see #registerUser()
     * @see AddSecurityQuestionController#setTempUser(User)
     * @see <a href="../../../../user/register/add-questions-security-view.fxml">add-questions-security-view.fxml</a>
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
     * Validates all user input fields in the registration form.
     * <p>
     * This includes validation for first name, last name, email, password format,
     * password confirmation, and email uniqueness via {@link AuthenticationService#emailCheck(String)}.
     * Displays warning messages via {@link ShowAlertUtils#showError(String, String)} if any field is invalid.
     * </p>
     * @return {@code true} if all fields are valid and ready for next step; {@code false} otherwise
     * @see ShowAlertUtils#showError(String, String)
     * @see RegexValidator#validEmailAddress(String)
     * @see RegexValidator#validPassword(String)
     * @see AuthenticationService#emailCheck(String)
     * @see EmailAlreadyInUseException
     * @see User
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
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
