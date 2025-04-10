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

public class CardViewController implements Initializable {

    protected static final Logger logger = LogManager.getLogger(CardViewController.class);

    @FXML protected ComboBox<Deck> deckComboBox;
    @FXML protected ListView<Card> cardsList;
    @FXML protected TextField cardName;
    @FXML protected TextArea cardAnswer;

    protected Deck currentDeck;
    protected Card selectedCard;

    protected final IDeckDAO deckDAO = new SqliteDeckDAO();

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

    protected void loadCards() {
        if (currentDeck != null) {
            ICardDAO cardDAO = new SqliteCardDAO();
            List<Card> cards = cardDAO.getCardsForDeck(currentDeck);
            cardsList.getItems().setAll(cards);
        }
    }

    protected void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    protected void selectListViewItem() {
        selectedCard = cardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            cardName.setText(selectedCard.getQuestion());
            cardAnswer.setText(selectedCard.getAnswer());
        }
    }
}
