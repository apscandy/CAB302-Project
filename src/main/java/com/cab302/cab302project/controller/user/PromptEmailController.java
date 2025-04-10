package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.EmailAlreadyInUseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PromptEmailController {

    @FXML
    private TextField userEmail;

    @FXML
    private Button goToPromptPasswordPageBtn;

    @FXML
    private Button goToRegisterPageBtn;

    @FXML
    public void goToPromptPasswordPage() throws IOException {
        String email = userEmail.getText();
        Authentication authHandler = new Authentication();
        try {
            boolean emailCheck = authHandler.emailCheck(email);
        } catch (EmailAlreadyInUseException e) {
            Stage stage = (Stage) goToPromptPasswordPageBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-password-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } // catch (EmailEmptyException e) UI handling...
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        Stage stage = (Stage) goToRegisterPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
