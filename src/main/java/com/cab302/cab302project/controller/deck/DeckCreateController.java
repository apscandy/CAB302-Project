package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckCreateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DeckCreateController.class);

    @FXML
    private final IDeckDAO deckDAO;

    @FXML
    private ListView<Deck> decks;

    @FXML
    private Button backButton;

    @FXML
    private TextField deckName;

    @FXML
    private TextArea deckDescription;



    public DeckCreateController(){
        deckDAO = new SqliteDeckDAO();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        logger.info("initializing DeckCreateController listView");
        if (!ApplicationState.isUserLoggedIn()) return;
        loadDecks();
    }

    @FXML
    private void selectListViewItem() {
        logger.debug("Select ListView item");
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckName.setText(deck.getName());
        deckDescription.setText(deck.getDescription());
    }

    @FXML
    private void backButton() throws IOException {
        logger.debug("Back button pressed");
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        logger.debug("User on main screen");
    }

    @FXML
    private void clearButton() {
        logger.debug("Clear button pressed");
        deckName.clear();
        deckDescription.clear();
    }

    @FXML
    private void createDeck() {
        logger.debug("Create deck button pressed");
        if (!ApplicationState.isUserLoggedIn()) return;
        if (deckName.getText().isEmpty()) {
            deckName.setText("New Deck");
        }
        Deck deck = new Deck(deckName.getText(), deckDescription.getText(), ApplicationState.getCurrentUser());
        deckDAO.createDeck(deck);
        loadDecks();
        clearButton();
    }

    @FXML
    private void deleteDeck() {
        logger.debug("Delete deck button pressed");
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckDAO.deleteDeck(deck);
        loadDecks();
        deckName.clear();
        deckDescription.clear();
    }

    @FXML
    private void editDeck() {
        logger.debug("Edit deck button pressed");
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deck.setName(deckName.getText());
        deck.setDescription(deckDescription.getText());
        deckDAO.updateDeck(deck);
        loadDecks();
    }



    private void loadDecks() {
        logger.debug("Load deck");
        if (!ApplicationState.isUserLoggedIn()) return;
        decks.getItems().clear();
        decks.getItems().addAll(deckDAO.getDecks(ApplicationState.getCurrentUser()));
        decks.refresh();
    }

}
