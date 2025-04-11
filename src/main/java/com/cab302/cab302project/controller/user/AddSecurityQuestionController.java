package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;

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

public class AddSecurityQuestionController {

    // Updated to match the fx:id in the FXML file (lowercase)
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

    public void setTempUser(User user) {
        this.tempUser = user;
    }

    private final List<String> SecQuestionList = List.of(
            "Question 1",
            "Question 2",
            "Question 3",
            "Question 4",
            "Question 5"
    );

    @FXML
    public void initialize() {
        firstQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        secondQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        thirdQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));

        firstQuestionComboBox.setOnAction(e -> handleComboBoxChange(firstQuestionComboBox));
        secondQuestionComboBox.setOnAction(e -> handleComboBoxChange(secondQuestionComboBox));
        thirdQuestionComboBox.setOnAction(e -> handleComboBoxChange(thirdQuestionComboBox));
    }

    private boolean isUpdating = false;

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
    public void RegisterButtonAction() throws IOException {
        String questionOne = firstQuestionComboBox.getValue();
        String questionTwo = secondQuestionComboBox.getValue();
        String questionThree = thirdQuestionComboBox.getValue();

        String answerOne = FirstAnswerTextField.getText();
        String answerTwo = SecondAnswerTextField.getText();
        String answerThree = ThirdAnswerTextField.getText();

        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion(tempUser);
        userSecurityQuestion.setQuestionOne(questionOne);
        userSecurityQuestion.setAnswerOne(answerOne);
        userSecurityQuestion.setQuestionTwo(questionTwo);
        userSecurityQuestion.setAnswerTwo(answerTwo);
        userSecurityQuestion.setQuestionThree(questionThree);
        userSecurityQuestion.setAnswerThree(answerThree);

        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.register(tempUser);
        SqliteUserSecurityQuestionDAO dao = new SqliteUserSecurityQuestionDAO();
        dao.createQuestion(userSecurityQuestion);

        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void BackButtonAction() throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
