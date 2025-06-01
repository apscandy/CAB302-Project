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
 * Controller for the security question validation during password reset.
 * <p>
 * Handles the authentication step where users must correctly answer their
 * security questions to proceed with password reset. Displays the user's
 * configured security questions and validates their answers against stored
 * responses. Manages navigation between the password prompt screen and the
 * password reset screen, ensuring secure user verification before allowing
 * password changes. All validation errors are appropriately displayed to
 * the user with descriptive messages.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
 **/

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
     * <p>
     * Receives and stores the User object from the previous screen in the
     * password reset flow. This user object contains the necessary information
     * for security question validation and is passed forward to subsequent
     * screens in the reset process.
     * </p>
     *
     * @param user The user attempting to reset their password
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    public void passUser(User user) {
        this.user = user;
    }

    /**
     * Sets up the security question labels in the UI.
     * <p>
     * Populates the security question labels with the user's configured
     * questions retrieved from the database. Expects exactly two questions
     * in the provided list and displays them in the appropriate label
     * components for user interaction.
     * </p>
     *
     * @param QuestionList List of security questions for the user, must contain exactly 2 questions
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
     */
    public void initSecurityQuestion(List<String> QuestionList) {
        securityQuestionOne.setText(QuestionList.get(0));
        securityQuestionTwo.setText(QuestionList.get(1));
    }

    /**
     * Validates security question answers and navigates to password reset screen if correct.
     * <p>
     * Retrieves the user's answers from both text fields and validates them
     * against the stored security question responses using the AuthenticationService.
     * If validation succeeds, loads the password reset view and passes the user's
     * email to the ResetPasswordController. If validation fails, displays
     * appropriate error messages without proceeding. Handles both empty answer
     * scenarios and incorrect answer scenarios with specific error messages.
     * </p>
     *
     * @throws IOException if the password reset FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
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
     * <p>
     * Loads the password prompt view and passes the user's email back to the
     * PromptPasswordController, allowing the user to return to the previous
     * step in the password reset flow. This maintains the user's progress
     * and avoids requiring them to re-enter their email address. Logs the
     * navigation action for debugging purposes.
     * </p>
     *
     * @throws IOException if the password prompt FXML resource cannot be loaded
     * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
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