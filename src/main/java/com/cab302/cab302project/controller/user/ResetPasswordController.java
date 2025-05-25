package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.InvalidPasswordFormatException;
import com.cab302.cab302project.error.authentication.PasswordEmptyException;
import com.cab302.cab302project.error.authentication.UserNotFoundException;
import com.cab302.cab302project.model.user.User;
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
 * Controller for password reset functionality.
 * <p>
 * Handles the final step of the password reset process where users
 * enter their new password. Validates password requirements and
 * confirmation matching before updating the user's password in the
 * database. Provides navigation back to security questions if needed
 * and returns to the login flow upon successful password reset.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
 **/

public class ResetPasswordController {
    @FXML
    private PasswordField resetPasswordField;

    @FXML
    private PasswordField checkResetPasswordField;

    @FXML
    private Button resetPasswordBtn;

    @FXML
    private Button backToAnswerSecurityQuestionBtn;

    private String userEmail;

    private static final Logger logger = LogManager.getLogger(ResetPasswordController.class);

    /**
     * Sets the email of the user who is resetting their password.
     * <p>
     * Stores the user's email address to identify the correct account
     * when the password reset is submitted. This email is passed from
     * the previous step in the password reset flow.
     * </p>
     *
     * @param email The email address of the user
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Navigates back to the security questions screen.
     * <p>
     * Returns to the previous step in the password reset flow, reloading
     * the security questions view with the user's questions. Retrieves
     * the user data and security questions to properly initialize the
     * previous screen state.
     * </p>
     *
     * @throws IOException if the security questions FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    @FXML
    public void backToAnswerSecurityQuestion() throws IOException {
        Stage stage = (Stage) backToAnswerSecurityQuestionBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/reset-password/answer-security-questions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        AnswerSecurityQuestionController answerController = fxmlLoader.getController();
        List<String> questionList = new ArrayList<>();
        User user = PromptPasswordController.retrieveUserAndInitQuestions(userEmail, questionList);
        answerController.initSecurityQuestion(questionList);
        answerController.passUser(user);
        stage.setScene(scene);
    }

    /**
     * Handles the password reset submission.
     * <p>
     * Validates that both password fields match and that the new password
     * meets security requirements. If validation passes, updates the user's
     * password in the database and returns to the login screen. Shows
     * appropriate error messages for password mismatches, empty passwords,
     * invalid formats, or if the user is not found.
     * </p>
     *
     * @throws IOException if the login FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    @FXML
    public void resetPassword() throws IOException {
        String newPassword = resetPasswordField.getText();
        String confirmPassword = checkResetPasswordField.getText();
        AuthenticationService authService = new AuthenticationService();

        if (!newPassword.equals(confirmPassword)) {
            ShowAlertUtils.showError("Reset Password Error","Passwords do not match. Please try again.");
            logger.debug("Passwords do not match");
            return;
        }

        try {
            authService.resetPassword(userEmail, newPassword);
        } catch (PasswordEmptyException pee) {
            ShowAlertUtils.showError("Reset Password Error","Password is empty");
            logger.debug("Password is empty");
            return;
        } catch (UserNotFoundException unfe) {
            ShowAlertUtils.showError("Reset Password Error","User not found");
            logger.debug("User not found");
            return;
        } catch (InvalidPasswordFormatException ipfe) {
            ShowAlertUtils.showError("Reset Password Error","Please make sure your password meet the minimum requirements.");
            logger.debug("Invalid password format when resetting password");
            return;
        }
        Stage stage = (Stage) resetPasswordBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
