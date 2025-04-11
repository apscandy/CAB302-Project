package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.PasswordEmptyException;
import com.cab302.cab302project.error.authentication.PasswordComparisonException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PromptPasswordController {

    // The resetPasswordBtn, loginBtn and backToPromptEmailPageBtn are injected from your FXML.
    public Button resetPasswordBtn;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button loginBtn;

    @FXML
    private Button backToPromptEmailPageBtn;

    // Removed errorPasswordMessage since there's no such fx:id element in the FXML.
    // @FXML
    // private Label errorPasswordMessage;

    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    private static final Logger logger = LogManager.getLogger(PromptPasswordController.class);

    @FXML
    public void login() throws IOException {
        String password = userPassword.getText();
        AuthenticationService authHandler = new AuthenticationService();
        boolean authenticated = false;
        try {
            authenticated = authHandler.authenticate(userEmail, password);
        } catch (UserAlreadyLoggedInException ex) {
            showAlert("Login Error", "User already logged in.");
        } catch (PasswordEmptyException ex) {
            showAlert("Login Error", "Password cannot be empty.");
        } catch (PasswordComparisonException ex) {
            showAlert("Login Error", "Incorrect password. Please try again.");
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

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   the title of the alert dialog
     * @param message the error message to be displayed
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
