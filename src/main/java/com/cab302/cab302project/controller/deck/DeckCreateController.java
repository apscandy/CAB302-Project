package com.cab302.cab302project.controller.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.error.util.*;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.util.DeckCSVUtils;
import com.cab302.cab302project.util.ShowAlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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

    @FXML
    private ListView<Card> cardsList;

    // ─── NEW bookmark button ───────────────────────────────────────────────────────
    @FXML
    private Button bookmarkButton;
    // ──────────────────────────────────────────────────────────────────────────────

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

        // set the button text based on current bookmark flag
        bookmarkButton.setText(
                deck.isBookmarked()
                        ? "Bookmarked ★"
                        : "Bookmark ☆"
        );
        cardsList.getItems().clear();
        deck.setCards(new SqliteCardDAO().getCardsForDeck(deck));
        if (deck.getCards() != null) {
            cardsList.getItems().addAll(deck.getCards());
        }
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Deck");
        alert.setHeaderText("Are you sure you want to delete this Deck?");
        alert.setContentText("This action will put this deck into the recycle bin.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deckDAO.softDeleteDeck(deck);
                loadDecks();
                deckName.clear();
                deckDescription.clear();
            }
        });
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

    /**
     * Toggle the bookmarked state of the currently selected deck.
     * <p>
     * When invoked, this method will:
     * <ol>
     *   <li>Abort if no user is logged in or no deck is selected.</li>
     *   <li>Flip the deck’s bookmarked flag and persist the change via the DAO.</li>
     *   <li>Update the bookmark button’s label to either
     *       <code>"Bookmarked ★"</code> or <code>"Bookmark ☆"</code> accordingly.</li>
     * </ol>
     *
     * @author Monica Borg (n9802045)
     */
    @FXML
    private void toggleBookmark() {
        if (!ApplicationState.isUserLoggedIn()) return;

        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) return;

        boolean nowBookmarked = !deck.isBookmarked();
        deckDAO.setBookmarked(deck, nowBookmarked);

        // update button text immediately
        bookmarkButton.setText(nowBookmarked
                ? "Bookmarked ★"
                : "Bookmark ☆"
        );
    }

    /**
     * Export a Deck to a CSV.
     *<p>
     * CSV layout:<br>
     * Deck Name,Deck Description<br>
     * &lt;name&gt;,&lt;description&gt;<br><br>
     *
     * Question,Answer,Tags<br>
     * "question 1","answer 1","tag1;tag2"<br>
     * "question 2","answer 2",""<br>
     * ...
     *</p>
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @FXML
    private void exportDeckCSV() {
        if (!ApplicationState.isUserLoggedIn()) return;
        Deck deck = decks.getSelectionModel().getSelectedItem();
        if (deck == null) {
            ShowAlertUtils.showWarning("Export Deck", "Select a deck to export.");
            return;
        }
        new SqliteCardDAO().getCardsForDeck(deck);
        if (deck.getCards() == null || deck.getCards().isEmpty()) {
            ShowAlertUtils.showWarning("Export Deck", "Deck has no card - Nothing to export.");
            return;
        }
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Export Deck as CSV");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName(deck.getName().replaceAll("\\s+", "_") + ".csv");
        File file = fileChooser.showSaveDialog(deckName.getScene().getWindow());
        if (file == null) return;
        try {
            DeckCSVUtils.exportDeck(file.getAbsolutePath(), deck);
            ShowAlertUtils.showInfo("Export Successful",
                    "Deck saved to:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            ShowAlertUtils.showError("Export Failed", e.getMessage());
        }
    }
}
