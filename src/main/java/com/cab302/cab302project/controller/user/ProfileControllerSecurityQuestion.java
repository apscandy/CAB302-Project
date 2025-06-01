package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.user.SqliteUserDAO;
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
 * Controller class responsible for handling the update of user security questions in a JavaFX application.
 * <p>
 * This class provides functionality to:
 * <ul>
 *     <li>Load and display the current user's existing security questions and answers</li>
 *     <li>Ensure that the user selects three distinct security questions</li>
 *     <li>Validate that all questions and answers are provided before submission</li>
 *     <li>Update the user's security questions and navigate back to the main view</li>
 * </ul>
 * </p>
 *
 * Dependencies:
 * <ul>
 *     <li>{@link SqliteUserDAO} – DAO for updating user data</li>
 *     <li>{@link SqliteUserSecurityQuestionDAO} – DAO for fetching and updating security questions</li>
 *     <li>{@link ShowAlertUtils} – Utility for displaying alert messages</li>
 * </ul>
 *
 * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
 */
public class ProfileControllerSecurityQuestion {

    // FXML fields mapped from the UI
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    @FXML private TextField FirstAnswerTextField;
    @FXML private TextField SecondAnswerTextField;
    @FXML private TextField ThirdAnswerTextField;

    @FXML private ComboBox<String> firstQuestionComboBox;
    @FXML private ComboBox<String> secondQuestionComboBox;
    @FXML private ComboBox<String> thirdQuestionComboBox;

    // DAOs for database operations
    private final SqliteUserDAO userDAO = new SqliteUserDAO();
    private final SqliteUserSecurityQuestionDAO usqDAO = new SqliteUserSecurityQuestionDAO();

    // List of available security questions
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

    private boolean isUpdating = false;

    /**
     * Loads and displays the currently logged-in user's existing security questions and answers
     * into the corresponding ComboBoxes and TextFields.
     * <p>
     * This method is called during initialization to populate the UI with the user's current
     * security question settings, allowing them to view or modify their answers.
     * </p>
     *
     * @param currentUser the currently logged-in user whose security questions will be loaded
     * @see SqliteUserSecurityQuestionDAO#getQuestions(User)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    @FXML
    public void loadUserSecurityQuestions(User currentUser) {

        UserSecurityQuestion currentQs = usqDAO.getQuestions(currentUser);
        firstQuestionComboBox.setValue(currentQs.getQuestionOne());
        secondQuestionComboBox.setValue(currentQs.getQuestionTwo());
        thirdQuestionComboBox.setValue(currentQs.getQuestionThree());
        FirstAnswerTextField.setText(currentQs.getAnswerOne());
        SecondAnswerTextField.setText(currentQs.getAnswerTwo());
        ThirdAnswerTextField.setText(currentQs.getAnswerThree());
        updateComboBoxes(null);

    }

    /**
     * Initializes the controller after the FXML elements have been loaded.
     * <p>
     * This method sets the available security questions in all ComboBoxes and sets up
     * change listeners to prevent duplicate selections. It also loads the currently logged-in
     * user's security questions and answers.
     * </p>
     * @see #loadUserSecurityQuestions(User)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    @FXML
    public void initialize() {
        firstQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        secondQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        thirdQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));

        firstQuestionComboBox.setOnAction(e -> handleComboBoxChange(firstQuestionComboBox));
        secondQuestionComboBox.setOnAction(e -> handleComboBoxChange(secondQuestionComboBox));
        thirdQuestionComboBox.setOnAction(e -> handleComboBoxChange(thirdQuestionComboBox));

        if (ApplicationState.getCurrentUser() != null) {
            loadUserSecurityQuestions(ApplicationState.getCurrentUser());
        }
    }

    /**
     * Handles the logic for preventing duplicate question selections when a ComboBox is changed.
     * <p>
     * When a user selects a new question in one ComboBox, the method updates the other ComboBoxes
     * to ensure that each question remains unique across the three selections.
     * </p>
     *
     * @param source the ComboBox that triggered the change event
     * @see #updateComboBoxes(ComboBox)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    private void handleComboBoxChange(ComboBox<String> source) {
        if (isUpdating) return;
        isUpdating = true;
        updateComboBoxes(source);
        isUpdating = false;
    }

    /**
     * Updates the list of available questions in each ComboBox to ensure that no two selections are the same.
     * <p>
     * If {@code changedBox} is {@code null}, all ComboBoxes are refreshed during initial setup.
     * Otherwise, only the other two ComboBoxes are updated based on the new selection.
     * </p>
     *
     * @param changedBox the ComboBox that was changed by the user, or {@code null} if called during initialization
     * @see #updateComboBox(ComboBox, String, Set)
     * @see #handleComboBoxChange(ComboBox)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    private void updateComboBoxes(ComboBox<String> changedBox) {
        String selected1 = firstQuestionComboBox.getValue();
        String selected2 = secondQuestionComboBox.getValue();
        String selected3 = thirdQuestionComboBox.getValue();

        Set<String> used = new HashSet<>();
        if (selected1 != null) used.add(selected1);
        if (selected2 != null) used.add(selected2);
        if (selected3 != null) used.add(selected3);

        if (changedBox != firstQuestionComboBox) {
            updateComboBox(firstQuestionComboBox, selected1, used);
        }
        if (changedBox != secondQuestionComboBox) {
            updateComboBox(secondQuestionComboBox, selected2, used);
        }
        if (changedBox != thirdQuestionComboBox) {
            updateComboBox(thirdQuestionComboBox, selected3, used);
        }
    }

    /**
     * Sets the items of the given ComboBox by filtering out already-used questions from other ComboBoxes.
     * <p>
     * This ensures that all ComboBox selections remain unique. The current selection is preserved
     * if it is still valid after filtering.
     * </p>
     *
     * @param box              the ComboBox to update
     * @param currentSelection the current value of the ComboBox
     * @param used             the set of all currently selected questions across ComboBoxes
     * @see #updateComboBoxes(ComboBox)
     * @see #handleComboBoxChange(ComboBox)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    private void updateComboBox(ComboBox<String> box, String currentSelection, Set<String> used) {
        Set<String> filteredUsed = new HashSet<>(used);
        filteredUsed.remove(currentSelection);

        List<String> available = SecQuestionList.stream()
                .filter(q -> !filteredUsed.contains(q))
                .toList();

        if (!box.getItems().equals(available)) {
            box.setItems(FXCollections.observableArrayList(available));
        }

        if (available.contains(currentSelection)) {
            box.setValue(currentSelection);
        } else {
            box.setValue(null);
        }
    }

    /**
     * Validates the user's input and updates their security questions and answers in the database.
     * <p>
     * The method checks that all fields are filled and that no duplicate questions are selected.
     * If validation succeeds, it saves the new data and redirects the user to the main view.
     * </p>
     * @see ShowAlertUtils#showWarning(String, String)
     * @see SqliteUserSecurityQuestionDAO#updateQuestions(UserSecurityQuestion)
     * @see SqliteUserDAO#updateUser(User)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    @FXML
    private void handleUpdateSecurityQuestion() {
        String q1 = firstQuestionComboBox.getValue();
        String q2 = secondQuestionComboBox.getValue();
        String q3 = thirdQuestionComboBox.getValue();
        String a1 = FirstAnswerTextField.getText();
        String a2 = SecondAnswerTextField.getText();
        String a3 = ThirdAnswerTextField.getText();

        if (q1 == null || q2 == null || q3 == null ||
                a1.isEmpty() || a2.isEmpty() || a3.isEmpty()) {
            ShowAlertUtils.showWarning("Validation Error", "All questions and answers must be filled.");
            return;
        }

        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion(ApplicationState.getCurrentUser());
        userSecurityQuestion.setQuestionOne(q1);
        userSecurityQuestion.setAnswerOne(a1);
        userSecurityQuestion.setQuestionTwo(q2);
        userSecurityQuestion.setAnswerTwo(a2);
        userSecurityQuestion.setQuestionThree(q3);
        userSecurityQuestion.setAnswerThree(a3);

        SqliteUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
        dao.updateQuestions(userSecurityQuestion);

        try {
            userDAO.updateUser(ApplicationState.getCurrentUser());
            ShowAlertUtils.showInfo("Success", "Security question updated successfully.");

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            ShowAlertUtils.showError("Error", "Failed to update security question: " + e.getMessage());
        }
    }

    /**
     * Cancels the update operation and navigates the user back to the main view.
     * <p>
     * If the view fails to load, an error alert is displayed.
     * </p>
     * @see ShowAlertUtils#showError(String, String)
     * @author Dang Linh Phan - Lewis (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    @FXML
    private void handleCancelUpdateSecurityQuestion() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ShowAlertUtils.showError("Error", "Failed to cancel: " + e.getMessage());
        }
    }

}
