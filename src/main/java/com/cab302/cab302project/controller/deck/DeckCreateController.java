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

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class DeckCreateController implements Initializable {

    @FXML
    private final IDeckDAO deckDAO;

    @FXML
    private ListView<Deck> decks;

    @FXML
    private Button newCardBtn;

    @FXML
    private Button editCardBtn;

    @FXML
    private TextField deckName;

    @FXML
    private TextArea deckDescription;

    private static final Logger logger = LogManager.getLogger(DeckCreateController.class);

    public DeckCreateController(){
        deckDAO = new SqliteDeckDAO();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param arg0
     * @param arg1
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
    private void selectListViewItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckName.setText(deck.getName());
        deckDescription.setText(deck.getDescription());
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void clearButton() {
        deckName.clear();
        deckDescription.clear();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void createDeck() {
        if (!ApplicationState.isUserLoggedIn()) return;
        if (deckName.getText().isEmpty()) {
            deckName.setText("New Deck");
        }
        Deck deck = new Deck(deckName.getText(), deckDescription.getText(), ApplicationState.getCurrentUser());
        deckDAO.createDeck(deck);
        loadDecks();
        clearButton();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void deleteDeck() {
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deckDAO.deleteDeck(deck);
        loadDecks();
        deckName.clear();
        deckDescription.clear();
    }

    @FXML
    private void newCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) newCardBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/new-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    @FXML
    private void editCard() throws IOException {
        if (!ApplicationState.isUserLoggedIn()) return;
        Stage stage = (Stage) editCardBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("card/edit-card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @FXML
    private void editDeck() {
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;
        deck.setName(deckName.getText());
        deck.setDescription(deckDescription.getText());
        deckDAO.updateDeck(deck);
        loadDecks();
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
