package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoang Dat Bui
 */
public class PromptPasswordController {

    @FXML
    public Button resetPasswordBtn;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button loginBtn;

    @FXML
    private Button backToPromptEmailPageBtn;
    
    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    private List<String> QuestionList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(PromptPasswordController.class);

    @FXML
    public void login() throws IOException {
        String password = userPassword.getText();
        AuthenticationService authHandler = new AuthenticationService();
        boolean authenticated = false;
        try {
            authenticated = authHandler.authenticate(userEmail, password);
        } catch (UserAlreadyLoggedInException ex) {
            logger.warn("User already logged in.");
        } catch (PasswordEmptyException ex) {
            logger.warn("Password cannot be empty.");
        } catch (PasswordComparisonException ex) {
            logger.warn("Incorrect password. Please try again.");
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
        User user = retrieveUserAndInitQuestions(userEmail, QuestionList);
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("answer-security-questions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        AnswerSecurityQuestionController answerController = fxmlLoader.getController();
        answerController.initSecurityQuestion(QuestionList);
        answerController.passUser(user);
        stage.setScene(scene);
    }

    public static User retrieveUserAndInitQuestions(String userEmail, List<String> QuestionList) {
        QuestionList.clear();
        SqliteUserDAO userDAO = new SqliteUserDAO();
        User user = userDAO.getUser(userEmail);
        SqliteUserSecurityQuestionDAO userSecurityQuestionDAO = new SqliteUserSecurityQuestionDAO();
        UserSecurityQuestion userSecurityQuestion = userSecurityQuestionDAO.getQuestions(user);
        QuestionList.add(userSecurityQuestion.getQuestionOne());
        QuestionList.add(userSecurityQuestion.getQuestionTwo());
        QuestionList.add(userSecurityQuestion.getQuestionThree());
        return user;
    }
}
