package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.controller.deck.DeckCreateController;
import com.cab302.cab302project.error.authenicaton.EmailAlreadyInUseException;
import com.cab302.cab302project.error.authenicaton.EmailEmptyException;
import com.cab302.cab302project.error.authenicaton.InvalidEmailFormatException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PromptEmailController {

    @FXML
    private TextField userEmail;

    @FXML
    private Button goToPromptPasswordPageBtn;

    @FXML
    private Button goToRegisterPageBtn;

    @FXML
    private Label errorEmailMessage;

    @FXML
    public void goToPromptPasswordPage() throws IOException {
        String email = userEmail.getText();
        Authentication authHandler = new Authentication();
        errorEmailMessage.setText("");
        try {
            authHandler.emailCheck(email);
        } catch (EmailAlreadyInUseException e) {
            errorEmailMessage.setText("Can't find your email. Please register");
            return;
        } catch (EmailEmptyException e) {
            errorEmailMessage.setText("Email cannot be empty.");
            return;
        } catch (InvalidEmailFormatException e) {
            errorEmailMessage.setText("Invalid email format.");
            return;
        }
        Stage stage = (Stage) goToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        Stage stage = (Stage) goToRegisterPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}