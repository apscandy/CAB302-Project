package com.cab302.cab302project;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final String TITLE = "CRAM IT!";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;
    private static Stage primaryStage; // NEW

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // This flag is for testing the ui to allow us to
        // test if all the ui is working by using in memory db
        // this flag will not be set under normal runs
        // add it to your run configuration `-t` in cli args
        try {
            if (args[0].equals("-t")) {
                SqliteConnection.setTestingModeTrue();
                new SqliteCreateTables();
                User user = new User("Monica", "Borg", "awhvdkyawvdky", "awudoguaywdv");
                IUserDAO userDAO = new SqliteUserDAO();
                userDAO.addUser(user);
                ApplicationState.login(user);
            }
        }catch (Exception ignored) {}
        new SqliteCreateTables();
        launch();
    }

    public static Stage getStage() { // NEW
        return primaryStage;
    }

}