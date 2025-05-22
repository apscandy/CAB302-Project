package com.cab302.cab302project.controller.card;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for creating, updating, and deleting cards.
 * <p>
 * Provides methods to save a new card or update an existing one, remove a card (soft delete),
 * clear input fields, load cards for the selected deck, and return to the deck view.
 * When returning, if a deck is selected in the ComboBox, the deck view (deck-view.fxml) is loaded and
 * the selected deck is displayed by programmatically selecting it in the deck view's ListView.
 * If no deck is selected, the deck creation view (deck-create-view.fxml) is loaded.
 * </p>
 *
 * @author Monica Borg (n9802045)
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
     * Initializes the controller by loading the available decks into the ComboBox and setting listeners to
     * load the cards for the selected deck and update the input fields when a card is selected.
     *
     * @param location  the location used to resolve relative paths for the root object, or null if unknown
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
     * Saves a new card or updates an existing card.
     * Validates that the question and answer fields are filled and that a deck is selected.
     */
    @FXML
    private void saveCard() {
        if (cardName.getText().isEmpty() || cardAnswer.getText().isEmpty()) {
            ShowAlertUtils.showError("Missing Fields", "Both question and answer must be filled in.");
            return;
        }
        if (currentDeck == null) {
            ShowAlertUtils.showError("No Deck Selected", "Please select a deck from the dropdown.");
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
     * Populates the fields for the selected card and then, when re-invoked,
     * saves any edits back to the database.
     */
    @FXML
    private void editCard() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard == null) {
            ShowAlertUtils.showError("No Card Selected", "Please select a card from the list to edit.");
            return;
        }

        String newQuestion = cardName.getText().trim();
        String newAnswer   = cardAnswer.getText().trim();
        if (newQuestion.isEmpty() || newAnswer.isEmpty()) {
            ShowAlertUtils.showError("Missing Fields", "Both question and answer must be filled in.");
            return;
        }
        selectedCard.setQuestion(newQuestion);
        selectedCard.setAnswer(newAnswer);

        try {
            cardDAO.updateCard(selectedCard);
        } catch (Exception e) {
            logger.error("Failed to update card", e);
            ShowAlertUtils.showError("Update Failed", "Could not save changes: " + e.getMessage());
            return;
        }

        clearCard();
        loadCards();
    }

    /**
     * Deletes the selected card via a soft delete after confirmation.
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
     * Clears the input fields and resets the selected card.
     */
    @FXML
    private void clearCard() {
        cardName.clear();
        cardAnswer.clear();
        selectedCard = null;
    }

    /**
     * Updates the input fields based on the card selected from the list.
     */
    @FXML
    private void selectListViewItem() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }

    /**
     * Returns to the deck view.
     * <p>
     * This method checks the deckComboBox for a selected deck.
     * If a deck is selected, it loads the deck view (deck-view.fxml) and then uses scene.lookup()
     * to find the ListView (fx:id "decks") in the deck view's root node. It then programmatically selects
     * the chosen deck so that the deck view displays that deck's details.
     * If no deck is selected, the method loads the standard deck view with no deck preloaded (deck-view.fxml).
     * </p>
     *
     * @throws IOException if the FXML resource cannot be loaded
     */
    @FXML
    private void returnToDeck() throws IOException {
        Stage stage = (Stage) deckComboBox.getScene().getWindow();
        FXMLLoader loader;
        if (deckComboBox.getSelectionModel().getSelectedItem() != null) {
            Deck selectedDeck = deckComboBox.getSelectionModel().getSelectedItem();
            loader = new FXMLLoader(HelloApplication.class.getResource("/com/cab302/cab302project/deck/deck-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, HelloApplication.WIDTH, HelloApplication.HEIGHT);
            ListView<Deck> deckList = (ListView<Deck>) root.lookup("#decks");
            if (deckList != null) {
                deckList.getSelectionModel().select(selectedDeck);
            }
            stage.setScene(scene);
        } else {
            loader = new FXMLLoader(HelloApplication.class.getResource("/com/cab302/cab302project/deck/deck-view.fxml"));
            Scene scene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }
        stage.show();
    }

    // ─── Merged in from CardViewController ───────────────────────────────────────

    /**
     * Loads cards for the currently selected deck and displays them in the cards list.
     * <p><strong>From:</strong> CardViewController</p>
     */
    private void loadCards() { // from CardViewController
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }
}
