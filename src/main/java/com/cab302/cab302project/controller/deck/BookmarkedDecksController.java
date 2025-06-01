package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookmarkedDecksController implements Initializable {

    private static final Logger logger = LogManager.getLogger(BookmarkedDecksController.class);

    @FXML private ListView<Deck> decks;
    @FXML private TextField deckName;
    @FXML private TextArea deckDescription;
    @FXML private Button bookmarkButton;
    @FXML private ListView<Card> cardsList;


    private final IDeckDAO deckDAO;
    private final ICardDAO cardDAO;

    public BookmarkedDecksController() {
        this.deckDAO = new SqliteDeckDAO();
        this.cardDAO = new SqliteCardDAO();
    }

        @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!ApplicationState.isUserLoggedIn()) return;
        loadBookmarkedDecks();
    }

    private void loadBookmarkedDecks() {
        decks.getItems().setAll(
                deckDAO.getBookmarkedDecks(ApplicationState.getCurrentUser())
        );
        decks.refresh();
    }

    /**
     * Called when a deck is selected from the bookmarked deck list.
     * <p>
     * Populates the deck name, description, and bookmark button label,
     * and loads all cards from the selected deck into the card ListView.
     * </p>
     * @author Monica Borg (n9802045) (monica.borg@connect.qut.edu.au)
     */
    @FXML
    private void selectListViewItem() {
        if (!ApplicationState.isUserLoggedIn()) return;

        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;

        deckName.setText(deck.getName());
        deckDescription.setText(deck.getDescription());
        // update remove‐bookmark label
        bookmarkButton.setText("Remove Bookmark " + (deck.isBookmarked() ? "★" : "☆"));

        cardsList.getItems().clear();
        cardDAO.getCardAndLoadIntoDeck(deck);
        if (deck.getCards() != null && !deck.getCards().isEmpty()) {
            cardsList.getItems().setAll(deck.getCards());
            cardsList.refresh();
        }
    }

    /**
     * Toggles the bookmark status for the selected deck.
     * <p>
     * If a deck is currently bookmarked, this method unmarks it,
     * removes it from the view, and clears all associated fields.
     * </p>
     * @author Monica Borg (n9802045) (monica.borg@connect.qut.edu.au)
     */
    @FXML
    private void toggleBookmark() {
        if (!ApplicationState.isUserLoggedIn()) return;

        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;

        deckDAO.setBookmarked(deck, false);
        decks.getItems().remove(deck);

        deckName.clear();
        deckDescription.clear();
        cardsList.getItems().clear();
    }

}
