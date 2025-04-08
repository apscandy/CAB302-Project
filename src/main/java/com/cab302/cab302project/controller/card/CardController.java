package com.cab302.cab302project.controller.card;

import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CardController {

    @FXML private ListView<Card> cardsList;
    @FXML private TextField cardName;
    @FXML private TextArea cardAnswer;

    private final ICardDAO cardDAO = new SqliteCardDAO();
    private Deck currentDeck;
    private Card selectedCard;

    public void setDeck(Deck deck) {
        this.currentDeck = deck;
        loadCards();
    }

    @FXML
    public void initialize() {
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

        if (selectedCard == null) {
            Card newCard = new Card(currentDeck, cardName.getText(), cardAnswer.getText(), null);
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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

    @FXML
    private void backButton() throws IOException {
        Stage stage = (Stage) cardsList.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("deck-view.fxml"));
        Scene scene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    private void loadCards() {
        if (currentDeck != null) {
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
