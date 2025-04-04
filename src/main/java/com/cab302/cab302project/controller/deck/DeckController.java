package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DeckController.class);

    @FXML
    private TextField deckName;

    @FXML
    private TextArea deckDescription;

    @FXML
    private ListView<Deck> decks;

    private final IDeckDAO deckDAO;

    public DeckController() {
        deckDAO = new SqliteDeckDAO();
    }

    public boolean userIsLoggedIn() {
        if (ApplicationState.getCurrentUser() == null || !ApplicationState.isUserLoggedIn()) {
            logger.warn("ApplicationState.getCurrentUser() is null");
            logger.warn("ApplicationState.isUserLoggedIn() equals false");
            logger.warn("Make sure the user is logged in");
            return false;
        }
        return true;
    }

    @FXML
    private void createDeck() {
        logger.debug("Create deck button pressed");
        if (!userIsLoggedIn()) return;
        Deck deck = new Deck(deckName.getText(), deckDescription.getText(), ApplicationState.getCurrentUser());
        deckDAO.createDeck(deck);
        loadDecks();
        deckName.clear();
        deckDescription.clear();
    }

    @FXML
    private void deleteDeck() {
        logger.debug("Delete deck button pressed");
        if (!userIsLoggedIn()) return;
        deckDAO.deleteDeck(decks.getSelectionModel().getSelectedItem());
        loadDecks();
    }

    @FXML
    private void editDeck() {
        logger.debug("Edit deck button pressed");
        if (!userIsLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        deck.setName(deckName.getText());
        deckDescription.setText(deckDescription.getText());
        deckDAO.updateDeck(deck);
        loadDecks();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        logger.info("initializing DeckController listView");
        if (!userIsLoggedIn()) return;
        loadDecks();
    }

    @FXML
    public void selectListViewItem() {
        if (!userIsLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckName.setText(deck.getName());
        deckDescription.setText(deck.getDescription());
    }

    private void loadDecks() {
        if (!userIsLoggedIn()) return;
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }
}
