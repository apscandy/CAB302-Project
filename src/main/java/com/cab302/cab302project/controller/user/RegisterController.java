package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
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

    public void NextButtonAction() throws IOException {
        System.out.println("Next button clicked");
        if (registerUser()) {
            System.out.println("User registration is valid, loading next scene...");
            Stage stage = (Stage) NextButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-questions-security-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } else {
            System.out.println("Registration failed, not changing scene");
        }
    }


    public boolean registerUser () {
        String firstName = FirstNameTextField.getText();
        String lastName = LastNameTextField.getText();
        String email = EmailAddressTextField.getText();
        String password = SetPasswordField.getText();
        String confirmPassword = ConfirmPasswordField.getText();

        // Reset text fields' prompt text
        FirstNameTextField.setPromptText("First Name");
        LastNameTextField.setPromptText("Last Name");
        EmailAddressTextField.setPromptText("Email Address");
        SetPasswordField.setPromptText("Password");
        ConfirmPasswordField.setPromptText("Confirm Password");

        String passwordRegex = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        boolean isValid = true;

        if (firstName == null || firstName.isBlank()) {
            FirstNameTextField.setPromptText("First name cannot be empty.");
            FirstNameTextField.setStyle("-fx-prompt-text-fill: red;");
            isValid = false;
        }

        if (lastName == null || lastName.isBlank()) {
            LastNameTextField.setPromptText("Last name cannot be empty.");
            LastNameTextField.setStyle("-fx-prompt-text-fill: red;");
            isValid = false;
        }

        if (!email.matches(emailRegex)) {
            EmailAddressTextField.setPromptText("Invalid email format");
            EmailAddressTextField.setStyle("-fx-prompt-text-fill: red;");
            isValid = false;
        }

        if (!password.matches(passwordRegex)) {
            SetPasswordField.setPromptText("Password must be at least 8 characters, include 1 number and 1 special character.");
            SetPasswordField.setStyle("-fx-prompt-text-fill: red;");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            ConfirmPasswordField.setPromptText("Password does not match!");
            ConfirmPasswordField.setStyle("-fx-prompt-text-fill: red;");
            isValid = false;
        }

        if (isValid) {
            ConfirmPasswordField.setPromptText("Registration info is valid!");
            ConfirmPasswordField.setStyle("-fx-prompt-text-fill: green;");
        }
        return isValid;
    }

}
