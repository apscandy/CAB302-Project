package com.cab302.cab302project.controller.menubar;

import com.cab302.cab302project.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MenuBarController {

    private static final Logger logger = LogManager.getLogger(MenuBarController.class);

    @FXML
    private MenuItem close;

    @FXML
    private void closeProgram() {
        logger.info("Close application button clicked");
        System.exit(0);
    }

    @FXML
    private void openDeckView() {
        logger.info("New -> Deck clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("deck/deck-view.fxml")); // ✅ FIXED

            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            HelloApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCardView() {
        logger.info("New -> Card clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("card/card-view.fxml")); // ✅ Adjust path
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            HelloApplication.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
