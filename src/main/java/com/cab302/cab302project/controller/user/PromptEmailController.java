package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private static final Logger logger = LogManager.getLogger(PromptEmailController.class);

    /**
     * Validates email and navigates to password screen if email exists.
     * Displays appropriate error messages for invalid or unregistered emails.
     * Passes the validated user's email to the PromptPassword Controller.
     * @author Hoang Dat Bui, Andrew Clarke
     */
    @FXML
    public void goToPromptPasswordPage() throws IOException {
        String email = userEmail.getText();
        AuthenticationService authHandler = new AuthenticationService();
        boolean isEmailFree = false;
        try {
            isEmailFree = authHandler.emailCheck(email);
        } catch (EmailEmptyException e) {
            ShowAlertUtils.showError("Prompt Email Error","Email cannot be empty.");
            return;
        } catch (InvalidEmailFormatException e) {
            ShowAlertUtils.showError("Prompt Email Error","Invalid email format.");
            return;
        } catch (EmailAlreadyInUseException ignored) {}
        if (isEmailFree) {
            // If it doesn't exist, display error message
            ShowAlertUtils.showError("Prompt Email Error","Email not found. Please register for an account.");
            return;
        }
        Stage stage = (Stage) goToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-password-view.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/register/register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
        logger.debug("User on register account screen");
    }

}
