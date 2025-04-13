package com.cab302.cab302project.controller.user;

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
    private Button CloseButton;
    @FXML
    private Button BackButton;

    private static User tempUser;

    public static void setTempUser(User user) {
        tempUser = user;
    }

    @FXML
    public void CloseButtonAction() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void NextButtonAction() throws IOException {
        if (registerUser()) {
            Stage stage = (Stage) NextButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-questions-security-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

            AddSecurityQuestionController addSecurityQuestionController = fxmlLoader.getController();
            addSecurityQuestionController.setTempUser(tempUser);
        }
    }

    public boolean registerUser() {
        String firstName = FirstNameTextField.getText();
        String lastName = LastNameTextField.getText();
        String email = EmailAddressTextField.getText();
        String password = SetPasswordField.getText();
        String confirmPassword = ConfirmPasswordField.getText();

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
        } else {
            try {
                new AuthenticationService().emailCheck(email);
            } catch (EmailAlreadyInUseException e) {
                setError(EmailAddressTextField, "Email address already in use");
                return false;
            }
        }

        // validate password
        if (password.isEmpty()) {
            setError(SetPasswordField, "Password cannot be empty.");
            return false;
        }
        if (!RegexValidator.validPassword(password)) {
            setError(SetPasswordField, "Password must be at least 8 characters, include 1 number and 1 special character.");
            return false;
        } else if (!password.equals(confirmPassword)) {
            setError(ConfirmPasswordField, "Passwords do not match.");
            return false;
        }

        User user = new User(firstName, lastName, email, password);
        setTempUser(user);
        return true;
    }

    private void setError(TextInputControl input, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
