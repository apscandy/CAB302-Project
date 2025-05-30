package com.cab302.cab302project;

import com.cab302.cab302project.model.SqliteCreateTables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Main application class for the CRAM IT! JavaFX application.
 * This class extends JavaFX's Application class and handles
 * the initialization of the SQLite database and the JavaFX stage.
 */
public class HelloApplication extends Application {

    /**
     * The title displayed in the application window.
     */
    public static final String TITLE = "CRAM IT!";

    /**
     * The width of the application window in pixels.
     */
    public static final int WIDTH = 640;

    /**
     * The height of the application window in pixels.
     */
    public static final int HEIGHT = 360;


    /**
     * JavaFX application start method.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If an error occurs loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user/login/prompt-email-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        new SqliteCreateTables();
        launch();
    }
}