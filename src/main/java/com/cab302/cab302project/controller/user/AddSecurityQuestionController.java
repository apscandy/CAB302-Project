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

    @FXML
    private ComboBox<String> FirstQuestionComboBox;
    @FXML
    private ComboBox<String> SecondQuestionComboBox;
    @FXML
    private ComboBox<String> ThirdQuestionComboBox;
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

    @FXML
    public void initialize() {

        FirstQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        SecondQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));
        ThirdQuestionComboBox.setItems(FXCollections.observableArrayList(SecQuestionList));

        FirstQuestionComboBox.setOnAction(e -> handleComboBoxChange(FirstQuestionComboBox));
        SecondQuestionComboBox.setOnAction(e -> handleComboBoxChange(SecondQuestionComboBox));
        ThirdQuestionComboBox.setOnAction(e -> handleComboBoxChange(ThirdQuestionComboBox));
    }

    private boolean isUpdating = false;

    private void handleComboBoxChange(ComboBox<String> source) {
        if (isUpdating) return;
        isUpdating = true;
        updateComboBoxes(source);
        isUpdating = false;

    }

    private void updateComboBoxes(ComboBox<String> changedBox) {
        String selected1 = FirstQuestionComboBox.getValue();
        String selected2 = SecondQuestionComboBox.getValue();
        String selected3 = ThirdQuestionComboBox.getValue();

        Set<String> used = new HashSet<>();
        if (selected1 != null) used.add(selected1);
        if (selected2 != null) used.add(selected2);
        if (selected3 != null) used.add(selected3);

        if (changedBox != FirstQuestionComboBox) {
            updateComboBox(FirstQuestionComboBox, selected1, used);
        }
        if (changedBox != SecondQuestionComboBox) {
            updateComboBox(SecondQuestionComboBox, selected2, used);
        }
        if (changedBox != ThirdQuestionComboBox) {
            updateComboBox(ThirdQuestionComboBox, selected3, used);
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
    public void RegisterButtonAction () throws IOException {

        String questionOne = FirstQuestionComboBox.getValue();
        String questionTwo = SecondQuestionComboBox.getValue();
        String questionThree = ThirdQuestionComboBox.getValue();

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
    public void BackButtonAction () throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
