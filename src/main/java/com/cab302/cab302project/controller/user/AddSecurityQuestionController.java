package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.ShowAlertUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * The {@code AddSecurityQuestionController} class provides methods to handle
 * button function in FXML file, controller for the screen to add security questions.
 * Manage the UI and handles logic when users selects a question and fill in the answer.
 * Once completed, saves the information to the database.
 * This class link to add-questions-security-view.fxml to control the Ul for add security questions.
 * <p>
 * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
 */
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

    private User tempUser;

    // Assign user from registration screen when registering security questions.
    public void setTempUser(User user) {
        this.tempUser = user;
    }

    // Fixed list of security questions to choose from
    private final List<String> SecQuestionList = List.of(
            "What city were you born in?",
            "What is your oldest sibling’s middle name?",
            "What was the first concert you attended?",
            "What was the make and model of your first car?",
            "In what city or town did your parents meet?",
            "What was your childhood best friend’s nickname?",
            "How many pets did you have at 10 years old?",
            "What’s your neighbor’s last name?",
            "In which city did your parents meet?",
            "What month did you get married?"
    );

    /**
     * Initialize ComboBox with list of security questions
     * Add event when user changes question, to prevent duplicate selection.
     */
    @FXML
    public void initialize() {
        // Assign the security question list to all 3 ComboBoxes.
        firstQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        secondQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        thirdQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));

        // Assign an event to each ComboBox to handle when the user changes the selection.
        firstQuestionComboBox.setOnAction(e -> handleComboBoxChange(firstQuestionComboBox));
        secondQuestionComboBox.setOnAction(e -> handleComboBoxChange(secondQuestionComboBox));
        thirdQuestionComboBox.setOnAction(e -> handleComboBoxChange(thirdQuestionComboBox));
    }

    private boolean isUpdating = false;

    /**
     * Handle the change event for any of the security question ComboBoxes.
     * <p>
     * This method ensures that when the user selects a new question in one ComboBox,
     * the other ComboBoxes are updated accordingly to prevent duplicate selections.
     * It uses a flag{@code isUpdating} to avoid recursive triggering of updates
     * when the ComboBox items are changed.
     * <p>
     * @param source the {@link ComboBox} that was changed by the users
     * @see #updateComboBoxes(ComboBox)
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
     */
    private void handleComboBoxChange(ComboBox<String> source) {
        if (isUpdating) return;
        isUpdating = true;
        updateComboBoxes(source);
        isUpdating = false;
    }

    /**
     * Updates the available items in all security question ComboBoxes to ensure that
     * each question can only be selected one.
     * <p>
     * This method checks the current sections in all three ComboBoxes and
     * temporarily filters out the selected questions from the other two ComboBoxes.
     * <p>
     * @param changedBox the {@link ComboBox} that trigger the change, used to skip its update
     * @see #updateComboBox(ComboBox, String, Set)
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
     */
    private void updateComboBoxes(ComboBox<String> changedBox) {

        // Get the currently selected value from all 3 ComboBoxes
        String selected1 = firstQuestionComboBox.getValue();
        String selected2 = secondQuestionComboBox.getValue();
        String selected3 = thirdQuestionComboBox.getValue();

        // Set of selected questions (null excluded)
        Set<String> used = new HashSet<>();
        if (selected1 != null) used.add(selected1);
        if (selected2 != null) used.add(selected2);
        if (selected3 != null) used.add(selected3);


        //Update the question list for the ComboBoxes, except for the one that was just changed
        // If ComboBox 1 is not the one that just changed, update it
        if (changedBox != firstQuestionComboBox) {
            updateComboBox(firstQuestionComboBox, selected1, used);
        }
        // If ComboBox 2 is not the one that just changed, update it
        if (changedBox != secondQuestionComboBox) {
            updateComboBox(secondQuestionComboBox, selected2, used);
        }
        // If ComboBox 3 is not the one that just changed, update it
        if (changedBox != thirdQuestionComboBox) {
            updateComboBox(thirdQuestionComboBox, selected3, used);
        }
    }

    /**
     * <p>
     * Updates a ComboBox with values that do not match other selected questions.
     * If the current questions is still valid, keep the selection; otherwise reset to null.
     * <p>
     * @param box: the {@link ComboBox} to update
     * @param currentSelection: the currently selected value of this ComboBox (can be null)
     * @param used: set of all selected questions across all ComboBoxes
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
     */
    private void updateComboBox(ComboBox<String> box, String currentSelection, Set<String> used) {
        // Create a copy of the selected question list to filter, keeping the current question of this ComboBox itself
        Set<String> filteredUsed = new HashSet<>(used);
        // Remove itself so it is not filtered out of the list
        filteredUsed.remove(currentSelection);

        // Filter out the list of selectable questions (not selected in other ComboBoxes)
        List<String> available = SecQuestionList.stream()
                .filter(q -> !filteredUsed.contains(q))
                .toList();

        // If the current item list is different from the new list, update the item
        if (!box.getItems().equals(available)) {
            box.setItems(FXCollections.observableArrayList(available));
        }

        // If the current question is still in the new list => keep it as is
        if (available.contains(currentSelection)) {
            box.setValue(currentSelection);
        } else {
            box.setValue(null);
        }
    }

    /**
     * Handles the event when the user clicks the "Register" button after selecting and filling in the answers to the
     * three security questions.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Checks if the user has selected all three security questions and filled in all three answer</li>
     * <li>If any field is missing, an error message is displayed asking to fill it in completely</li>
     * <li>If valid, create a {@link UserSecurityQuestion} object with the user information and security question.</li>
     * <li>Call the service to register the user account into the system.</li>
     * <li>Save security question information to the database via DAO.</li>
     * <li>Display a successful registration message and redirect the user to the home screen.</li>
     * </ul>
     * </p>
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au)
     */
    @FXML
    public void RegisterButtonAction() throws IOException {
        // get the user information entered the security question field.
        String questionOne = firstQuestionComboBox.getValue();
        String questionTwo = secondQuestionComboBox.getValue();
        String questionThree = thirdQuestionComboBox.getValue();

        // get the user information entered the answer field.
        String answerOne = FirstAnswerTextField.getText();
        String answerTwo = SecondAnswerTextField.getText();
        String answerThree = ThirdAnswerTextField.getText();

        // requires users to enter complete answers and select complete question to register
        boolean hasError = false;
        if (answerOne.isEmpty() || answerTwo.isEmpty() || answerThree.isEmpty()) {
            hasError = true;
        }
        if (questionOne == null) {
            hasError = true;
        }
        if (questionTwo == null) {
            hasError = true;
        }
        if (questionThree == null) {
            hasError = true;
        }
        if (hasError) {
            ShowAlertUtils.showWarning("validation Error", "Please fill in all security questions and answers.");
            return;
        }

        // Create a UserSecurityQuestion object to contain 3 questions and 3 answers
        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion(tempUser);
        userSecurityQuestion.setQuestionOne(questionOne);
        userSecurityQuestion.setAnswerOne(answerOne);
        userSecurityQuestion.setQuestionTwo(questionTwo);
        userSecurityQuestion.setAnswerTwo(answerTwo);
        userSecurityQuestion.setQuestionThree(questionThree);
        userSecurityQuestion.setAnswerThree(answerThree);

        // Register user into the system
        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.register(tempUser);

        // Save security question information to database
        SqliteUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
        dao.createQuestion(userSecurityQuestion);

        // Display success message to user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Security Questions Complete");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully registered and are now logged in.");
        alert.showAndWait();

        // Navigate the user to the main interface of the application
        ApplicationState.login(tempUser);
        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Return to the registration screen if the user presses Back.
     * @author Dang Linh Phan - Lewis (danglinh.phan@connect.qut.edu.au) or (phandanglinh2005@gmail.com)
     */
    @FXML
    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/register/register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

}
