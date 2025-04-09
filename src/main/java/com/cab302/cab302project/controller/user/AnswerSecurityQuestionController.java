package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.IUserSecurityQuestionDAO;

import java.io.IOException;

public class AnswerSecurityQuestionController {
    @FXML
    private TextField AnswerOneTextField;

    @FXML
    private TextField AnswerTwoTextField;

    @FXML
    private TextField AnswerThreeTextField;

    private User user;

    @FXML
    private Button backToPromptPasswordPageBtn;

    @FXML
    private Button goToResetPasswordPageBtn;

    public void setUser(User user) {
        this.user = user;
    }

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

    private boolean validateAnswers() {
        if (user != null) {
            IUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
            UserSecurityQuestion userSecQuestions = dao.getQuestions(user);

            String answerOne = AnswerOneTextField.getText().trim();
            String answerTwo = AnswerTwoTextField.getText().trim();
            String answerThree = AnswerThreeTextField.getText().trim();

            return answerOne.equals(userSecQuestions.getAnswerOne()) &&
                    answerTwo.equals(userSecQuestions.getAnswerTwo()) &&
                    answerThree.equals(userSecQuestions.getAnswerThree());
        }
        return false;
    }

    @FXML
    public void initialize() {
        if (user != null) {
            IUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
            UserSecurityQuestion userSecQuestions = dao.getQuestions(user);

            AnswerOneTextField.setPromptText(userSecQuestions.getQuestionOne());
            AnswerTwoTextField.setPromptText(userSecQuestions.getQuestionTwo());
            AnswerThreeTextField.setPromptText(userSecQuestions.getQuestionThree());
        } else {
            System.out.println("Error: User object is null");
        }
    }


}
