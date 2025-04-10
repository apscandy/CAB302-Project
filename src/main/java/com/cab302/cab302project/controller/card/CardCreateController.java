package com.cab302.cab302project.controller.card;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardCreateController extends CardViewController {

    private static final Logger logger = LogManager.getLogger(CardCreateController.class);
    private final ICardDAO cardDAO = new SqliteCardDAO();

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
}
