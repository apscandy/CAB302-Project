package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
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

/**
 * @author Hoang Dat Bui
 */
public class PromptEmailController {

    @FXML
    private TextField userEmail;

    @FXML
    private Button goToPromptPasswordPageBtn;

    @FXML
    private Button goToRegisterPageBtn;

    @FXML
    private Label errorEmailMessage;

    private static final Logger logger = LogManager.getLogger(PromptEmailController.class);

    @FXML
    public void goToPromptPasswordPage() throws IOException {
        String email = userEmail.getText();
        AuthenticationService authHandler = new AuthenticationService();
        errorEmailMessage.setText("");
        boolean isEmailFree = false;
        try {
            isEmailFree = authHandler.emailCheck(email);
        } catch (EmailEmptyException e) {
            errorEmailMessage.setText("Email cannot be empty.");
            return;
        } catch (InvalidEmailFormatException e) {
            errorEmailMessage.setText("Invalid email format.");
            return;
        } catch (EmailAlreadyInUseException ignored) {}
        if (isEmailFree) {
            // If it doesn't exist, display error message
            errorEmailMessage.setText("Email not found. Please register for an account.");
            return;
        }
        Stage stage = (Stage) goToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        PromptPasswordController passwordController = fxmlLoader.getController();
        passwordController.setUserEmail(email);
        stage.setScene(scene);
        logger.debug("User on prompt password view screen");
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        logger.debug("Go to register account screen button pressed");
        Stage stage = (Stage) goToRegisterPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
        logger.debug("User on register account screen");
    }
}
