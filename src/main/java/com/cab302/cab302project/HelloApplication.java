package com.cab302.cab302project;

import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final String TITLE = "Address book";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private IUserDAO userDAO;

    @Override
    public void start(Stage stage) throws IOException {
        // <--- Below is temporary until users are done --->
        // if you wish to use this uncomment below and run it once, then open the db file and add a single user
//        new SqliteCreateTables();
//        userDAO = new SqliteUserDAO();
//        ApplicationState.login(userDAO.getUser("waDa"));
//        userDAO.addUser(new User("andy", "clarke", "waDa","123142"));
//        User user = new User("Andrew", "Clarke", "thegoat@qut.edu.au", "Password");
//        user.setId(1);
        // <--- End --->
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
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