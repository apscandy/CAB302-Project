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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.stage.Stage;
import com.cab302.cab302project.util.RandomModeUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuBarController {

    private static final Logger logger = LogManager.getLogger(MenuBarController.class);

    @FXML
    private MenuItem close;

    @FXML
    private HBox rootHBox;

    @FXML
    private void closeProgram() {
        logger.info("Close application button clicked");
        System.exit(0);
    }

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

    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
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

    @FXML
    private void clearRecycleBin() {

    }

    @FXML
    private void changeEmailButton() {
        switchScene("change-email-view.fxml");
    }

    @FXML
    private void changePasswordButton() {
        switchScene("change-password-view.fxml");
    }

    @FXML
    private void changeSecurityQuestionButton() {
        switchScene("change-security-questions-view.fxml");
    }

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

    @FXML
    private void runRandomMode() throws IOException {
        Deck selectedDeck = ApplicationState.getCurrentDeck();
        if (selectedDeck == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Deck Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a deck before starting Random Mode.");
            alert.showAndWait();
        }
        else {
            ICardDAO cardDAO = new SqliteCardDAO();
            cardDAO.getCardAndLoadIntoDeck(selectedDeck);
            List<Card> shuffledCards = RandomModeUtils.getShuffledCards(selectedDeck.getCards());

            Stage stage = (Stage) rootHBox.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deck/random-mode-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
            RandomModeController controller = fxmlLoader.getController();
            controller.setShuffledCards(shuffledCards);
        }

    }

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
            showAlert(Alert.AlertType.ERROR, "Import Failed", e.getMessage());
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
            showAlert(Alert.AlertType.INFORMATION, "Import Successful",
                    "Imported deck \"" + deck.getName() + "\"");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Could not save imported deck to the database.\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
