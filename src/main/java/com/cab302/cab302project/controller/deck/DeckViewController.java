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
 * Controller class for managing deck views in the application.
 * This class handles user interactions related to decks, including
 * creating new decks, viewing existing decks, and managing cards within decks.
 *
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

    /**
     * Logger for logging messages related to this class.
     */
    private static final Logger logger = LogManager.getLogger(DeckViewController.class);

    /**
     * Database access object for deck operations.
     */
    private final IDeckDAO deckDAO;

    /**
     * Database access object for card operations.
     */
    private final ICardDAO cardDAO;

    /**
     * Constructor that initializes the database DAOs.
     * This class is responsible for managing interactions between
     * the UI and the underlying data storage (SQLite).
     */
    public DeckViewController() {
        deckDAO = new SqliteDeckDAO();
        cardDAO = new SqliteCardDAO();
    }

    /**
     * Handles the "Create Deck" button click event.
     * Loads the deck view FXML file to display the deck management interface.
     *
     * @throws IOException if there's an issue loading the FXML file
     */
    @FXML
    private void createDeck() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("deck/deck-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * Handles the "Create Card" button click event.
     * Loads the new card creation view FXML file to display the card management interface.
     *
     * @throws IOException if there's an issue loading the FXML file
     */
    @FXML
    private void createCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/new-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * Handles the "Edit Card" button click event.
     * Loads the card editing view FXML file to display the card management interface.
     *
     * @throws IOException if there's an issue loading the FXML file
     */
    @FXML
    private void editCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) createDeckButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/edit-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * This method is called when the view is first created and loads all decks
     * for the currently logged-in user.
     *
     * @param arg0  The location used to resolve relative paths for the FXML file.
     * @param arg1 The resources used to localize the application.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        if (!ApplicationState.isUserLoggedIn()) return;
        loadDecks();
    }

    /**
     * Handles the selection of a deck from the deck list view.
     * Updates the card list to show all cards belonging to the selected deck.
     */
    @FXML
    private void selectDeckListViewItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        cards.getItems().clear();
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;

        ApplicationState.setDeck(deck);

        cardDAO.getCardAndLoadIntoDeck(deck);
        ApplicationState.setDeck(deck);
        if (deck.getCards().size() >= 1) {
            cards.getItems().clear();
            cards.getItems().addAll(deck.getCards());
            cards.refresh();
        }
    }

    /**
     * Handles the selection of a card from the card list view.
     * Updates the card list to show all cards belonging to the currently selected deck.
     */
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
     * Loads all decks for the currently logged-in user into the deck list view.
     * This method clears any existing decks and refreshes the display.
     */
    private void loadDecks() {
        if (!ApplicationState.isUserLoggedIn()) return;
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }

}
