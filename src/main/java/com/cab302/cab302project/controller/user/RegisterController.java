package com.cab302.cab302project.controller.user;
import com.cab302.cab302project.util.RegexValidator;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.controller.user.Authentication;
import com.cab302.cab302project.error.authenicaton.*;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.util.RegexValidator;
import com.cab302.cab302project.controller.user.AddSecurityQuestionController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Label LastNameLabel;
    @FXML
    private Label FirstNameLabel;
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private TextField LastNameTextField;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private Label EmailTypeLabel;
    @FXML
    private PasswordField SetPasswordField;
    @FXML
    private PasswordField ConfirmPasswordField;
    @FXML
    private Label SetPasswordLabel;
    @FXML
    private Label ConfirmPasswordLabel;
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
    public void CloseButtonAction () {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    public void BackButtonAction () throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void NextButtonAction () throws IOException {
        if (registerUser()) {
            Stage stage = (Stage) NextButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-questions-security-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

            AddSecurityQuestionController addSecurityQuestionController = fxmlLoader.getController();
            addSecurityQuestionController.setTempUser(tempUser);
        }
    }

    public boolean registerUser () {
        String firstName = FirstNameTextField.getText();
        String lastName = LastNameTextField.getText();
        String email = EmailAddressTextField.getText();
        String password = SetPasswordField.getText();
        String confirmPassword = ConfirmPasswordField.getText();



        // Reset error labels
        clearErrorLabels();
        boolean isValid = true;

        if (firstName.isEmpty()) {
            setError(FirstNameLabel, "First name cannot be empty.");
            isValid = false;
        }

        if (lastName.isEmpty()) {
            setError(LastNameLabel, "Last name cannot be empty.");
            isValid = false;
        }

        if (!RegexValidator.validEmailAddress(email)) {
            setError(EmailTypeLabel, "Invalid email format.");
            isValid = false;
        }

        if (!RegexValidator.validPassword(password)) {
            setError(SetPasswordLabel, "Password must be at least 8 characters, include 1 number and 1 special character.");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            setError(ConfirmPasswordLabel, "Passwords do not match.");
            isValid = false;
        }

        if (!isValid) return false;



        try {
            User newUser = new User(firstName, lastName, email, password);
            setTempUser(newUser);
            ConfirmPasswordLabel.setText("Registration successful!");
            return true;
        } catch (EmailAlreadyInUseException e) {
            setError(EmailTypeLabel, "Email is already in use.");
        } catch (EmailEmptyException e) {
            setError(EmailTypeLabel, "Email cannot be empty.");
        } catch (InvalidEmailFormatException e) {
            setError(EmailTypeLabel, "Invalid email format.");
        } catch (PasswordEmptyException e) {
            setError(SetPasswordLabel, "Password cannot be empty.");
        } catch (InvalidPasswordFormatException e) {
            setError(SetPasswordLabel, "Password does not meet requirements.");
        } catch (RuntimeException e) {
            setError(ConfirmPasswordLabel, "Unexpected error: " + e.getMessage());
        }

        return false;
    }

    private void setError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    private void clearErrorLabels() {
        FirstNameLabel.setText("");
        LastNameLabel.setText("");
        EmailTypeLabel.setText("");
        SetPasswordLabel.setText("");
        ConfirmPasswordLabel.setText("");
    }
}
