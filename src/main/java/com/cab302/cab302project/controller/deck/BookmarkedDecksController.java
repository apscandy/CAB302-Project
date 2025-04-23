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
     * Called when the user clicks on the left‐hand list of decks.
     * Populates the title, description, bookmark button text and loads the cards.
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
     * Fired when “Remove Bookmark ☆” is clicked.
     * Unmarks the deck then removes it from the list.
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
