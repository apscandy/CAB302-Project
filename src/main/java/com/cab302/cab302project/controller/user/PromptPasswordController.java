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
 * Controller for password authentication and password reset flow.
 * <p>
 * Handles password validation for user login and provides navigation to
 * password reset functionality through security questions. Authenticates
 * users with their stored passwords and manages the flow between login,
 * password reset, and main application screens. Displays appropriate
 * error messages for authentication failures.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
 **/
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
     * <p>
     * Stores the validated email address from the previous screen for use
     * in authentication and password reset operations.
     * </p>
     *
     * @param email The email address of the user
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Authenticates the user with the provided password.
     * <p>
     * Validates the password against stored credentials using AuthenticationService.
     * On successful authentication, navigates to the main application screen.
     * Shows error messages for empty passwords, incorrect passwords, or if
     * the user is already logged in.
     * </p>
     *
     * @throws IOException if the main application FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
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

    /**
     * Navigates back to the email prompt screen.
     * <p>
     * Returns to the previous step in the login flow, allowing users
     * to change their email address or restart the authentication process.
     * </p>
     *
     * @throws IOException if the email prompt FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    @FXML
    public void backToPromptEmailPage() throws IOException {
        Stage stage = (Stage) backToPromptEmailPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Navigates to the security questions screen.
     * <p>
     * Initiates the password reset flow by retrieving the user's security
     * questions and navigating to the answer validation screen. Passes both
     * the user object and security questions to the next controller.
     * </p>
     *
     * @throws IOException if the security questions FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
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
     * Retrieves a user and adds their security questions to a provided list.
     * <p>
     * Fetches the user from the database using their email address and
     * populates the question list with their three configured security
     * questions for use in the password reset flow.
     * </p>
     *
     * @param userEmail The email of the user
     * @param QuestionList List to populate with the user's security questions
     * @return The retrieved User object
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
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