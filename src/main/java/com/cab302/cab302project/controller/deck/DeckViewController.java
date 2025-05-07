package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class DeckViewController implements Initializable {

    @FXML
    private Label deckName;

    @FXML
    private TextArea deckDescription;

    @FXML
    private Button createDeckButton;

    @FXML
    private ListView<Deck> decks;

    @FXML
    private ListView<Card> cards;

    private static final Logger logger = LogManager.getLogger(DeckViewController.class);

    private final IDeckDAO deckDAO;

    private final ICardDAO cardDAO;
    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public DeckViewController() {
        deckDAO = new SqliteDeckDAO();
        cardDAO = new SqliteCardDAO();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void createDeck() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deck/deck-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    @FXML
    private void createCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/new-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    @FXML
    private void editCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/edit-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        if (!ApplicationState.isUserLoggedIn()) return;
        loadDecks();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void selectDeckListViewItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        cards.getItems().clear();
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;

        ApplicationState.setCurrentDeck(deck);

        cardDAO.getCardAndLoadIntoDeck(deck);
        if (deck.getCards().size() >= 1) {
            cards.getItems().clear();
            cards.getItems().addAll(deck.getCards());
            cards.refresh();
        }
    }

    @FXML
    private void selectCardListViewItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        cards.getItems().clear();
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        cardDAO.getCardAndLoadIntoDeck(deck);
        if (deck.getCards().size() >= 1) {
            cards.getItems().clear();
            cards.getItems().addAll(deck.getCards());
            cards.refresh();
        }
    }



    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void loadDecks() {
        if (!ApplicationState.isUserLoggedIn()) return;
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }

}
