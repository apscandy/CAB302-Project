package com.cab302.cab302project;

import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final String TITLE = "Flashcard app";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;


    @Override
    public void start(Stage stage) throws IOException {
        // <--- Below is temporary until users are done --->
        // if you wish to use this uncomment below and run it once, then open the db file and add a single user
//        new SqliteCreateTables();
//        User user = new User("Andrew", "Clarke", "thegoat@qut.edu.au", "Password");
//        user.setId(1);
//        ApplicationState.login(user);
//        UserSecurityQuestion question = new UserSecurityQuestion(user);
//        SqliteUserDAO userDAO = new SqliteUserDAO();
//        userDAO.addUser(user);
//        question.setQuestionThree("dfghj");
//        question.setQuestionOne("ksdvnjdfv");
//        question.setQuestionTwo("yefgeuren");
//        question.setAnswerThree("dfghj");
//        question.setAnswerOne("ksdvnjdfv");
//        question.setAnswerTwo("yefgeuren");
//        SqliteUserSecurityQuestionDAO questionDAO = new SqliteUserSecurityQuestionDAO();
//        questionDAO.createQuestion(question);
        // <--- End --->
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}