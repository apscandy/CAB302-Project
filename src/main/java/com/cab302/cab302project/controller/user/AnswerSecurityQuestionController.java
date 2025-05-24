package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * @author Hoang Dat Bui
 */

public class AnswerSecurityQuestionController {
    @FXML
    private TextField AnswerOneTextField;

    @FXML
    private TextField AnswerTwoTextField;

    @FXML
    private Label securityQuestionOne;

    @FXML
    private Label securityQuestionTwo;

    @FXML
    private Button backToPromptPasswordPageBtn;

    @FXML
    private Button goToResetPasswordPageBtn;

    private static final Logger logger = LogManager.getLogger(AnswerSecurityQuestionController.class);

    private User user;

    /**
     * Stores the user object for this controller to use.
     *
     * @param user The user attempting to reset their password
     */
    public void passUser(User user) {
        this.user = user;
    }

    /**
     * Sets up the security question labels in the UI.
     *
     * @param QuestionList List of security questions for the user
     */
    public void initSecurityQuestion(List<String> QuestionList) {
        securityQuestionOne.setText(QuestionList.get(0));
        securityQuestionTwo.setText(QuestionList.get(1));
    }

    /**
     * Validates security question answers and navigates to password reset screen if correct.
     * Displays appropriate error messages if validation fails.
     * Passes the validated user's email to the ResetPasswordController.
     */
    @FXML
    public void goToResetPasswordPage() throws IOException {
        String answer1 = AnswerOneTextField.getText();
        String answer2 = AnswerTwoTextField.getText();

        AuthenticationService authHandler = new AuthenticationService();
        try {
            authHandler.checkSecurityQuestion(user, answer1, answer2);
        } catch (EmptyAnswerException e) {
            ShowAlertUtils.showError("Answer Security Question Error","Answers cannot be empty.");
            logger.debug("Answers cannot be empty.");
            return;
        } catch (FailedQuestionException e) {
            ShowAlertUtils.showError("Answer Security Question Error","Incorrect answer. Please try again.");
            logger.debug("Incorrect answer. Please try again.");
            return;
        }
        Stage stage = (Stage) goToResetPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/reset-password/reset-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        ResetPasswordController resetController = fxmlLoader.getController();
        resetController.setUserEmail(user.getEmail());
        stage.setScene(scene);
        logger.debug("goToResetPasswordPage button pressed");
    }

    /**
     * Navigates back to the password prompt screen.
     * Preserves the user's email in the previous screen.
     */
    @FXML
    public void backToPromptPasswordPage() throws IOException {
        Stage stage = (Stage) backToPromptPasswordPageBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-password-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        PromptPasswordController promptController = fxmlLoader.getController();
        promptController.setUserEmail(user.getEmail());
        stage.setScene(scene);
        logger.debug("backToPromptPasswordPage button pressed");
    }

}
