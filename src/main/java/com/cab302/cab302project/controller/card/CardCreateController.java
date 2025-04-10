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
import javafx.scene.control.ButtonType;
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
 * Controller for creating, updating, and deleting cards.
 * <p>
 * This controller provides methods to save a new card or update an existing one,
 * delete (soft delete) a card, clear the input fields, and load the cards for the selected deck.
 * </p>
 *
 * @author Monica Borg
 * @version n9802045
 */
public class CardCreateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(CardCreateController.class);

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
     * If a user is logged in, loads the decks into the combo box and sets a listener to load cards
     * when a deck is selected. Also sets up a listener on the list of cards to populate the text fields.
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
     * Loads the cards for the currently selected deck and populates the cards list.
     */
    private void loadCards() {
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title the title for the alert dialog
     * @param msg   the message for the alert dialog
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Saves a new card or updates an existing card.
     * <p>
     * Validates that the card question and answer are filled in, and that a deck is selected.
     * If no card is selected, a new card is created; otherwise, the existing card is updated.
     * </p>
     */
    @FXML
    private void saveCard() {
        if (cardName.getText().isEmpty() || cardAnswer.getText().isEmpty()) {
            showAlert("Missing Fields", "Both question and answer must be filled in.");
            return;
        }
        if (currentDeck == null) {
            showAlert("No Deck Selected", "Please select a deck from the dropdown.");
            return;
        }
        if (selectedCard == null) {
            Card newCard = new Card(currentDeck, cardName.getText(), cardAnswer.getText(), "");
            cardDAO.addCard(newCard);
        } else {
            selectedCard.setQuestion(cardName.getText());
            selectedCard.setAnswer(cardAnswer.getText());
            cardDAO.updateCard(selectedCard);
        }
        clearCard();
        loadCards();
    }

    /**
     * Loads the details of the selected card into the text fields for editing.
     */
    @FXML
    private void editCard() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }

    /**
     * Deletes the selected card with a soft delete.
     * <p>
     * Prompts the user for confirmation before soft deleting the card.
     * </p>
     */
    @FXML
    private void deleteCard() {
        if (selectedCard != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Flashcard");
            alert.setHeaderText("Are you sure you want to delete this card?");
            alert.setContentText("This action will hide the card from your deck (soft delete).");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    cardDAO.softDeleteCard(selectedCard);
                    clearCard();
                    loadCards();
                }
            });
        }
    }

    /**
     * Clears the card input fields and resets the selected card.
     */
    @FXML
    private void clearCard() {
        cardName.clear();
        cardAnswer.clear();
        selectedCard = null;
    }

    /**
     * Updates the text fields with the details of the card selected from the list.
     */
    @FXML
    private void selectListViewItem() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }
}
