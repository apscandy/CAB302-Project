package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.userSecQuestions.IUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.cab302.cab302project.model.user.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AddSecurityQuestionController {

    @FXML
    private ComboBox<String> firstQuestionComboBox;
    @FXML
    private ComboBox<String> secondQuestionComboBox;
    @FXML
    private ComboBox<String> thirdQuestionComboBox;
    @FXML
    private TextField FirstAnswerTextField;
    @FXML
    private TextField SecondAnswerTextField;
    @FXML
    private TextField ThirdAnswerTextField;
    @FXML
    private Button BackButton;
    @FXML
    private Button RegisterButton;

    private List<String> securityQuestions = Arrays.asList(
            "What is your mother's maiden name?",
            "What was the name of your first pet?",
            "What was your high school nickname?",
            "In what city were you born?",
            "What is your favorite colour?"
    );

    @FXML
    public void initialize() {
        firstQuestionComboBox.getItems().addAll(securityQuestions);
        secondQuestionComboBox.getItems().addAll(securityQuestions);
        thirdQuestionComboBox.getItems().addAll(securityQuestions);
    }

    @FXML
    public void RegisterButtonAction() throws IOException {
        User user = ApplicationState.getCurrentUser();

        String answerOne = FirstAnswerTextField.getText();
        String answerTwo = SecondAnswerTextField.getText();
        String answerThree = ThirdAnswerTextField.getText();

        String questionOne = firstQuestionComboBox.getValue();
        String questionTwo = secondQuestionComboBox.getValue();
        String questionThree = thirdQuestionComboBox.getValue();

        UserSecurityQuestion userSecQuestions = new UserSecurityQuestion(user);
        userSecQuestions.setQuestionOne(questionOne);
        userSecQuestions.setAnswerOne(answerOne);
        userSecQuestions.setQuestionTwo(questionTwo);
        userSecQuestions.setAnswerTwo(answerTwo);
        userSecQuestions.setQuestionThree(questionThree);
        userSecQuestions.setAnswerThree(answerThree);

        IUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
        dao.createQuestion(userSecQuestions);

        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
