package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Hoang Dat Bui
 */
public class PromptPasswordController {

    public Button resetPasswordBtn;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button loginBtn;

    @FXML
    private Button backToPromptEmailPageBtn;

    @FXML
    private Label errorPasswordMessage;

    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    private static final Logger logger = LogManager.getLogger(PromptPasswordController.class);

    @FXML
    public void login() throws IOException {
        errorPasswordMessage.setText("");
        String password = userPassword.getText();
        AuthenticationService authHandler = new AuthenticationService();
        boolean authenticated = false;
        try {
            authenticated = authHandler.authenticate(userEmail, password);
        } catch (UserAlreadyLoggedInException ex) {
            errorPasswordMessage.setText("User already logged in.");
        } catch (PasswordEmptyException ex) {
            errorPasswordMessage.setText("Password cannot be empty.");
        } catch (PasswordComparisonException ex) {
            errorPasswordMessage.setText("Incorrect password. Please try again.");
        }
        if (authenticated) {
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }
    }

    @FXML
    public void backToPromptEmailPage() throws IOException {
        Stage stage = (Stage) backToPromptEmailPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void goToAnswerSecurityQuestion() throws IOException {
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("answer-security-questions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
