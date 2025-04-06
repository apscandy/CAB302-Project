package com.cab302.cab302project.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private TextField LastNameTextField;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private TextField SetPasswordField;
    @FXML
    private TextField ConfirmPasswordField;
    @FXML
    private Button NextButton;
    @FXML
    private Button CloseButton;

    @FXML
    public void CloseButtonAction () {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    public void nextButtonAction () {

    }
}
