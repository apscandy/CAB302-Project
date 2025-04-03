package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeckController {

    @FXML
    private TextField deckName;

    @FXML
    private TextField deckDescription;

    private final IDeckDAO deckDAO;

    public DeckController() {
        deckDAO = new SqliteDeckDAO();
    }

    @FXML
    private void createDeck() {
        if (!ApplicationState.isUserLoggedIn()) throw new RuntimeException("User is not logged in");
        Deck deck =  new Deck(deckName.getText(), deckDescription.getText(), ApplicationState.getCurrentUser());
        deckDAO.createDeck(deck);
    }
}
