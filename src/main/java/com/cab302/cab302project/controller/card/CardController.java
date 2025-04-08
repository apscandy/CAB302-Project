package com.cab302.cab302project.controller.card;

import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CardController {

    @FXML private TextField questionField;
    @FXML private TextArea answerField;
    @FXML private TextField tagsField;
    @FXML private ListView<Card> cardListView;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    private final ICardDAO cardDAO = new SqliteCardDAO();
    private Deck currentDeck;
    private Card selectedCard;

    public void setDeck(Deck deck) {
        this.currentDeck = deck;
        loadCards();
    }

    @FXML
    public void initialize() {
        cardListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCard = newSelection;
                questionField.setText(newSelection.getQuestion());
                answerField.setText(newSelection.getAnswer());
                tagsField.setText(newSelection.getTags());
            }
        });
    }

    @FXML
    private void onSave() {
        if (questionField.getText().isEmpty() || answerField.getText().isEmpty()) {
            showAlert("Missing Fields", "Please fill in both the question and answer fields.");
            return;
        }

        if (selectedCard == null) {
            Card newCard = new Card(currentDeck, questionField.getText(), answerField.getText(), tagsField.getText());
            cardDAO.addCard(newCard);
        } else {
            selectedCard.setQuestion(questionField.getText());
            selectedCard.setAnswer(answerField.getText());
            selectedCard.setTags(tagsField.getText());
            cardDAO.updateCard(selectedCard);
        }

        clearForm();
        loadCards();
    }

    @FXML
    private void onDelete() {
        if (selectedCard != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Are you sure you want to delete this card?");
            alert.setContentText("This will remove the card from this deck.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    cardDAO.softDeleteCard(selectedCard);
                    clearForm();
                    loadCards();
                }
            });
        }
    }

    private void loadCards() {
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardListView.getItems().setAll(cards);
        }
    }

    private void clearForm() {
        questionField.clear();
        answerField.clear();
        tagsField.clear();
        selectedCard = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
