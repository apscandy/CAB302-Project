package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.ShowAlertUtils;
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

    private final List<String> QuestionList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(PromptPasswordController.class);

    /**
     * Sets the email of the user attempting to authenticate for this controller to use.
     *
     * @param email The email address of the user
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Authenticates the user with the provided password.
     * Navigates to the main application if authentication succeeds.
     */
    @FXML
    public void login() throws IOException {
        String password = userPassword.getText();
        AuthenticationService authHandler = new AuthenticationService();
        boolean authenticated = false;
        try {
            authenticated = authHandler.authenticate(userEmail, password);
        } catch (UserAlreadyLoggedInException ex) {
            ShowAlertUtils.showError("Prompt Password Error","User already logged in.");
            logger.debug("User already logged in.");
        } catch (PasswordEmptyException ex) {
            ShowAlertUtils.showError("Prompt Password Error","Password cannot be empty.");
            logger.debug("Password cannot be empty.");
        } catch (PasswordComparisonException ex) {
            ShowAlertUtils.showError("Prompt Password Error","Incorrect password. Please try again.");
            logger.debug("Incorrect password. Please try again.");
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Navigates to the security questions screen.
     * Loads the next view and initialises it with the user's security questions.
     */
    @FXML
    public void goToAnswerSecurityQuestion() throws IOException {
        User user = retrieveUserAndInitQuestions(userEmail, QuestionList);
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/reset-password/answer-security-questions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        AnswerSecurityQuestionController answerController = fxmlLoader.getController();
        answerController.initSecurityQuestion(QuestionList);
        answerController.passUser(user);
        stage.setScene(scene);
    }

    /**
     * Retrieves a user and add their security questions to a provided list.
     *
     * @param userEmail The email of the user
     * @param QuestionList List to populate with the user's security questions
     * @return The retrieved User object
     */
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