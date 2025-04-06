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

    public void NextButtonAction () throws IOException {
        if (registerUser()) {
            Stage stage = (Stage) NextButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-questions-security-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }
    }

    public boolean registerUser () {
        String email = EmailAddressTextField.getText();
        String password = SetPasswordField.getText();
        String confirmPassword = ConfirmPasswordField.getText();

        String passwordRegex = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        boolean isValid = true;

        if(!email.matches(emailRegex)) {
            EmailTypeLabel.setText("Invalid email format");
            EmailTypeLabel.setVisible(true);
            isValid = false;
        }
        if (!password.matches(passwordRegex)) {
            ConfirmPasswordLabel.setText("Password must be at least 8 characters, include 1 number and 1 special character.");
            ConfirmPasswordLabel.setTextFill(javafx.scene.paint.Color.RED);
            ConfirmPasswordLabel.setVisible(true);
            isValid = false;
        }
        else if (!password.equals(confirmPassword)) {
            ConfirmPasswordLabel.setText("Password does not match!");
            ConfirmPasswordLabel.setTextFill(javafx.scene.paint.Color.RED);
            ConfirmPasswordLabel.setVisible(true);
            isValid = false;
        }
        else {
            ConfirmPasswordLabel.setText("You are set");
            ConfirmPasswordLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            ConfirmPasswordLabel.setVisible(true);
            isValid = true ;
        }
        return isValid;
    }
}
