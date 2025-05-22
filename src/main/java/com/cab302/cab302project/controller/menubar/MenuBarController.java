package com.cab302.cab302project.controller.menubar;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.controller.deck.RandomModeController;
import com.cab302.cab302project.controller.user.AddSecurityQuestionController;
import com.cab302.cab302project.error.util.*;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.util.DeckCSVUtils;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the application-wide menu bar.
 * <p>
 * Provides handlers for menu items such as navigation to Home, Deck
 * management, Card creation, test modes, profile management, import/export,
 * and application exit. All scene changes are performed via FXMLLoader,
 * and the user’s authentication and application state are respected.
 * </p>
 * @author Andrew Clarke, Monica Borg, Maverick Doan, David Bui, Lewis Phan
 **/
public class MenuBarController {

    private static final Logger logger = LogManager.getLogger(MenuBarController.class);

    @FXML
    private MenuItem close;

    @FXML
    private HBox rootHBox;

    /**
     * Exits the application immediately.
     * <p>
     * Logs the action and then calls {@link System#exit(int)} with status 0.
     * </p>
     * @author Andrew Clarke (n11270179)
     */
    @FXML
    private void closeProgram() {
        logger.info("Close application button clicked");
        System.exit(0);
    }

    /**
     * Navigates to the Home view.
     * <p>
     * Loads 'main/main.fxml' and sets it as the current scene
     * on the primary stage. Any exceptions during FXML loading are printed.
     * </p>
     * @author Monica Borg (n09802045)
     */
    @FXML
    private void home() {
        logger.info("Home clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("main/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts Random test mode.
     * <p>
     * Checks that a deck is selected in {@link ApplicationState}. If none is selected,
     * shows a warning alert. Otherwise, sets the current mode to Random,
     * loads 'test-mode/test-mode.fxml', and displays it.
     * </p>
     *
     * @throws IOException if the FXML resource cannot be loaded
     * @author Maverick Doan (n11562773)
     */
    @FXML
    private void goToTestModeRandom() throws IOException {
        if (ApplicationState.getDeck() == null) {
            ShowAlertUtils.showWarning("No Deck Selected", "Please select a deck before continuing");
            return;
        }
        try {
            ApplicationState.setCurrentModeRandom();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("test-mode/test-mode.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts Smart test mode.
     * <p>
     * Checks for deck selection, shows warning if none, then
     * sets mode to Smart and navigates to 'test-mode/test-mode.fxml'.
     * </p>
     *
     * @throws IOException if the FXML resource cannot be loaded
     * @author Maverick Doan (n11562773)
     */
    @FXML
    private void goToTestModeSmart() throws IOException {
        if (ApplicationState.getDeck() == null) {
            ShowAlertUtils.showWarning("No Deck Selected", "Please select a deck before continuing");
            return;
        }
        try {
            ApplicationState.setCurrentModeSmart();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("test-mode/test-mode.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts Standard (sequential) test mode.
     * <p>
     * Validates deck selection, sets mode to Sequential, and loads
     * 'test-mode/test-mode.fxml'. Exceptions are caught and logged.
     * </p>
     * @author Andrew Clarke (n11270179)
     */
    @FXML
    private void goToTestModeStandard() {
        if (ApplicationState.getDeck() == null) {
            ShowAlertUtils.showWarning("No Deck Selected", "Please select a deck before continuing");
            return;
        }
        try {
            ApplicationState.setCurrentModeSequential();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("test-mode/test-mode.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Deck management view.
     * <p>
     * Loads 'deck/deck-view.fxml' for creating, editing, or deleting decks.
     * </p>
     * @author Monica Borg (n09802045)
     */
    @FXML
    private void openDeckView() {
        logger.info("New -> Deck clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("deck/deck-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Card creation view.
     * <p>
     * Navigates to 'card/new-card-view.fxml' for adding new flip cards.
     * </p>
     * @author Monica Borg (n09802045)
     */
    @FXML
    private void openCardView() {
        logger.info("New -> Card clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("card/new-card-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Bookmarked Decks view.
     * <p>
     * Loads 'deck/bookmarked-decks.fxml' to display decks marked as bookmarked.
     * </p>
     * @author Monica Borg (n09802045)
     */
    @FXML
    private void openBookmarkView() {
        logger.info("File -> Bookmark clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("deck/bookmarked-decks.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs out the current user and returns to the login prompt.
     * <p>
     * Clears the application state, then loads 'prompt-email-view.fxml'.
     * </p>
     * @author Monica Borg (n09802045)
     */
    @FXML
    private void logOut() {
        logger.info("Log Out clicked");
        ApplicationState.logout();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("prompt-email-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enters the Recycle Bin view.
     * <p>
     * Loads 'recyclebin/recycle-bin-view.fxml' to allow permanent deletion or restoration.
     * </p>
     * @author David Bui (n11659831)
     */
    @FXML
    private void enterRecycleBin() {
        logger.info("Recycle bin clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("recyclebin/recycle-bin-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to the Change Email scene.
     * <p>
     * Uses {@link #switchScene(String)} with 'change-email-view.fxml'.
     * </p>
     * @author Lewis Phan (n11781840)
     */
    @FXML
    private void changeEmailButton() {
        switchScene("change-email-view.fxml");
    }

    /**
     * Switches to the Change Password scene.
     * <p>
     * Uses {@link #switchScene(String)} with 'change-password-view.fxml'.
     * </p>
     * @author Lewis Phan (n11781840)
     */
    @FXML
    private void changePasswordButton() {
        switchScene("change-password-view.fxml");
    }

    /**
     * Switches to the Change Security Questions scene.
     * <p>
     * Uses {@link #switchScene(String)} with 'change-security-questions-view.fxml'.
     * </p>
     * @author Lewis Phan (n11781840)
     */
    @FXML
    private void changeSecurityQuestionButton() {
        switchScene("change-security-questions-view.fxml");
    }

    /**
     * Deletes the current user’s account after confirmation.
     * <p>
     * Prompts with a CONFIRMATION dialog; if confirmed, calls
     * {@link SqliteUserDAO#deleteUser(User)} and logs out, then
     * navigates back to the login prompt.</p>
     * @author Lewis Phan (n11781840)
     */
    @FXML
    private void DeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Account Deletion");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == deleteButton) {
                try {
                    User currentUser = ApplicationState.getCurrentUser();
                    SqliteUserDAO sqliteUserDAO  = new SqliteUserDAO();
                    sqliteUserDAO.deleteUser(currentUser);

                    logger.info("User account deleted: " + currentUser.getEmail());

                    ApplicationState.logout();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prompt-email-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
                    Stage primaryStage = (Stage) rootHBox.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (Exception e) {
                    logger.error("Error deleting account", e);
                }
            }
        });
    }

    /**
     * Helper to switch scenes by FXML path.
     * <p>
     * Attempts to load the given FXML file from the classpath and set it as the
     * current scene on the primary stage. Logs an error on failure.
     * </p>
     *
     * @param fxmlPath the relative path to an FXML resource under the package
     * @author Lewis Phan (n11781840)
     */
    private void switchScene(String fxmlPath) {
        try {

            Stage stage = (Stage) rootHBox.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            logger.error("Failed to switch scene: " + fxmlPath, e);
        }
    }

    /**
     * Imports a Deck from a CSV file.
     * <p>
     * Prompts the user to select a CSV via {@link FileChooser}, then uses
     * {@link DeckCSVUtils#importDeck(String, User)} to parse and build a Deck.
     * Inserts the Deck and its Cards into the database, showing alerts for
     * success or any encountered errors.</p>
     * @author Maverick Doan (n11562773)
     */
    @FXML
    private void importDeckCSV() {
        if (!ApplicationState.isUserLoggedIn()) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Deck CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(rootHBox.getScene().getWindow());
        if (file == null) return;
        Deck deck;
        try {
            deck = DeckCSVUtils.importDeck(file.getAbsolutePath(),
                    ApplicationState.getCurrentUser());
        } catch (CSVImportException |
                 CSVImportInvalidFormatException |
                 InvalidCSVContentException |
                 FilePathIsNullException |
                 InvalidFilePathException e) {
            ShowAlertUtils.showError("Import Failed", e.getMessage());
            return;
        }
        IDeckDAO deckDAO = new SqliteDeckDAO();
        ICardDAO cardDAO = new SqliteCardDAO();
        try {
            deckDAO.createDeck(deck);
            if (deck.getCards() != null) {
                for (Card c : deck.getCards()) {
                    cardDAO.addCard(c);
                }
            }
            ShowAlertUtils.showInfo("Import Successful",
                    "Imported deck \"" + deck.getName() + "\"");

        } catch (Exception e) {
            ShowAlertUtils.showError("Database Error",
                    "Could not save imported deck to the database.\n" + e.getMessage());
        }
    }
}
