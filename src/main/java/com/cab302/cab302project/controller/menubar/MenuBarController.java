package com.cab302.cab302project.controller.menubar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuBarController {

    private static final Logger logger = LogManager.getLogger(MenuBarController.class);

    @FXML
    private MenuItem close;

    @FXML
    private void closeProgram() {
        logger.info("Close application button clicked");

    }
}
