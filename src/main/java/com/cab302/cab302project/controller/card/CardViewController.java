package com.cab302.cab302project.controller.card;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for viewing cards.
 * <p>
 * This controller is responsible for populating the deck ComboBox,
 * listing the cards for the selected deck, and displaying the details of the selected card.
 * </p>
 *
 * @author Monica Borg
 * @version n9802045
 */
public class CardViewController implements Initializable {

    private static final Logger logger = LogManager.getLogger(CardViewController.class);

    @FXML private ComboBox<Deck> deckComboBox;
    @FXML private ListView<Card> cardsList;
    @FXML private TextField cardName;
    @FXML private TextArea cardAnswer;

    private Deck currentDeck;
    private Card selectedCard;

    private final IDeckDAO deckDAO = new SqliteDeckDAO();
    private final ICardDAO cardDAO = new SqliteCardDAO();

    /**
     * Initializes the controller.
     * <p>
     * Populates the deckComboBox with decks created by the logged-in user and adds listeners
     * to update the card list and display the details of a selected card.
     * </p>
     *
     * @param location  the location used to resolve relative paths for the root object, or null if not known
     * @param resources the resources used to localize the root object, or null if not specified
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ApplicationState.isUserLoggedIn()) {
            List<Deck> decks = deckDAO.getDecks(ApplicationState.getCurrentUser());
            deckComboBox.setItems(FXCollections.observableArrayList(decks));
            deckComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldDeck, newDeck) -> {
                currentDeck = newDeck;
                loadCards();
            });
        }
        cardsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCard = newSelection;
                cardName.setText(selectedCard.getQuestion());
                cardAnswer.setText(selectedCard.getAnswer());
            }
        });
    }

    /**
     * Loads cards for the currently selected deck and displays them in the cards list.
     */
    private void loadCards() {
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title the title for the alert dialog
     * @param msg   the message for the alert dialog
     */
    protected void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Updates the text fields with the selected card's question and answer.
     */
    @FXML
    protected void selectListViewItem() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }
}
