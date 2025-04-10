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

public class CardController implements Initializable {

    private static final Logger logger = LogManager.getLogger(CardController.class);

    @FXML private ComboBox<Deck> deckComboBox;
    @FXML private ListView<Card> cardsList;
    @FXML private TextField cardName;
    @FXML private TextArea cardAnswer;

    private final ICardDAO cardDAO = new SqliteCardDAO();
    private final IDeckDAO deckDAO = new SqliteDeckDAO();
    private Deck currentDeck;
    private Card selectedCard;

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

    @FXML
    private void editCard() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }

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

    @FXML
    private void clearCard() {
        cardName.clear();
        cardAnswer.clear();
        selectedCard = null;
    }

    private void loadCards() {
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void selectListViewItem() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }
}
