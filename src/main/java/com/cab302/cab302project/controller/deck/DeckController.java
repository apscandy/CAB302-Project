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

import java.net.URL;
import java.util.ResourceBundle;

public class DeckController implements Initializable {

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

    @FXML
    private void createDeck() {
        if (!ApplicationState.isUserLoggedIn()) throw new RuntimeException("User is not logged in");
        Deck deck =  new Deck(deckName.getText(), deckDescription.getText(), ApplicationState.getCurrentUser());
        deckDAO.createDeck(deck);
        loadDecks();
        deckName.clear();
        deckDescription.clear();
    }

    @FXML
    private void deleteDeck() {
        if (!ApplicationState.isUserLoggedIn()) throw new RuntimeException("User is not logged in");
        deckDAO.deleteDeck(decks.getSelectionModel().getSelectedItem());
        loadDecks();

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        if (!ApplicationState.isUserLoggedIn()) throw new RuntimeException("User is not logged in");
        loadDecks();
    }

    private void loadDecks() {
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }
}
