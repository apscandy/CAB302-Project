package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AnswerSecurityQuestionController {
    @FXML
    private TextField AnswerOneTextField;

    @FXML
    private TextField AnswerTwoTextField;

    @FXML
    private TextField AnswerThreeTextField;

    @FXML
    private Button backToPromptPasswordPageBtn;

    @FXML
    private Button goToResetPasswordPageBtn;

    @FXML
    public void backToPromptPasswordPage() throws IOException {
        Stage stage = (Stage) backToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void goToResetPasswordPage() throws IOException {
        Stage stage = (Stage) goToResetPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("reset-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
