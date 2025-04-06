package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.ApplicationState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userPassword;

    @FXML
    private Button resetPasswordBtn;

    @FXML
    private Button goToPromptPasswordPageBtn;

    @FXML
    private Button backToPromptEmailPageBtn;

    @FXML
    public void resetPassword() throws IOException {
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("answer-security-questions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void backToPromptEmailPage() throws IOException {
        Stage stage = (Stage) backToPromptEmailPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void goToPromptPasswordPage() throws IOException {
        Stage stage = (Stage) goToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void login() {

    }
    
    public static void authenticate (String email, String password, SqliteUserDAO userDAO) {
        User user = userDAO.getUser(email);
        if (user == null) {
            return;
        }
        if (user.getPassword().equals(password)) {
            ApplicationState.login(user);
        }
    }
}
