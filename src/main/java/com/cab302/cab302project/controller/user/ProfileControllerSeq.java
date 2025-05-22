package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.authentication.EmailAlreadyInUseException;
import com.cab302.cab302project.error.model.question.FailedToGetQuestionsException;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.PasswordUtils;
import com.cab302.cab302project.util.RegexValidator;
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

public class ProfileControllerSeq {

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    @FXML private TextField FirstAnswerTextField;
    @FXML private TextField SecondAnswerTextField;
    @FXML private TextField ThirdAnswerTextField;

    @FXML private ComboBox<String> firstQuestionComboBox;
    @FXML private ComboBox<String> secondQuestionComboBox;
    @FXML private ComboBox<String> thirdQuestionComboBox;

    private final SqliteUserDAO userDAO = new SqliteUserDAO();
    private final SqliteUserSecurityQuestionDAO usqDAO = new SqliteUserSecurityQuestionDAO();

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

    private void handleComboBoxChange(ComboBox<String> source) {
        if (isUpdating) return;
        isUpdating = true;
        updateComboBoxes(source);
        isUpdating = false;
    }

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
