package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckViewController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DeckViewController.class);

    @FXML
    private Label deckName;

    @FXML
    private TextArea deckDescription;

    @FXML
    private Button createDeckButton;

    @FXML
    private ListView<Deck> decks;

    private final IDeckDAO deckDAO;

    public DeckViewController() {
        deckDAO = new SqliteDeckDAO();
    }

    private static boolean userIsLoggedIn() {
        if (ApplicationState.getCurrentUser() == null || !ApplicationState.isUserLoggedIn()) {
            logger.warn("ApplicationState.getCurrentUser() is null");
            logger.warn("ApplicationState.isUserLoggedIn() equals false");
            logger.warn("Make sure the user is logged in");
            return false;
        }
        return true;
    }

    @FXML
    private void createDeck() throws IOException {
        logger.debug("Create deck button pressed");
        if (!userIsLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deck/deck-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        logger.debug("User on deck view screen");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        logger.info("initializing DeckController listView");
        if (!userIsLoggedIn()) return;
        loadDecks();
    }

    @FXML
    public void selectListViewItem() {
        logger.debug("Select ListView item");
        if (!userIsLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckName.setVisible(true);
        deckDescription.setVisible(true);
        deckName.setText(deck.getName());
        deckDescription.setText(deck.getDescription());
    }

    private void loadDecks() {
        logger.debug("Load deck");
        if (!userIsLoggedIn()) return;
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }
}
