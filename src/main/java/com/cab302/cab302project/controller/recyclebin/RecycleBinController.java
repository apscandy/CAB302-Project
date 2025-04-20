package com.cab302.cab302project.controller.recyclebin;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.controller.deck.DeckCreateController;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Hoang Dat Bui
 */
public class RecycleBinController implements Initializable {

    @FXML
    private final IDeckDAO deckDAO;

    @FXML
    private final ICardDAO cardDAO;

    @FXML
    private ListView<Object> recycleBinList;

    private Object selectedItem;

    public RecycleBinController() {
        this.deckDAO = new SqliteDeckDAO();
        this.cardDAO = new SqliteCardDAO();
    }

    private static final Logger logger = LogManager.getLogger(RecycleBinController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!ApplicationState.isUserLoggedIn()) return;
        // Set up the list cell factory to display both Card and Deck objects properly
        recycleBinList.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<Object>() {
            @Override
            public String toString(Object object) {
                if (object instanceof Deck deck) {
                    return deck.getName();
                } else if (object instanceof Card card) {
                    return "["+ card.getDeck().getName() +"] " + card.getQuestion();
                }
                return object.toString();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        }));

        reloadList();
    }

    @FXML
    private void selectItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        selectedItem = recycleBinList.getSelectionModel().getSelectedItem();
        logger.info("An item is selected");
    }

    /**
     * Permanently deletes the selected item from the database.
     */
    @FXML
    private void deleteAnItem() {
        if (selectedItem == null) {
            showError("No Selection", "Please select an item to delete.");
            return;
        }

        if (selectedItem instanceof Deck deck) {
            showConfirmation("Delete Deck", "Are you sure you want to permanently delete this deck?",
                    "This action will delete the deck and all its cards forever.", response -> {
                        if (response == ButtonType.OK) {
                            deckDAO.deleteDeck(deck);
                            cardDAO.deleteCardsByDeck(deck);
                            reloadList();
                            logger.info("A deck has been deleted");
                        }
                    });
        } else if (selectedItem instanceof Card card) {
            showConfirmation("Delete Card", "Are you sure you want to permanently delete this card?",
                    "This action will delete the card forever.", response -> {
                        if (response == ButtonType.OK) {
                            cardDAO.deleteCard(card);
                            reloadList();
                            logger.info("A card has been deleted");
                        }
                    });
        }
    }

    /**
     * Restores the selected item by setting is_deleted to false.
     */
    @FXML
    private void restoreAnItem() {
        if (selectedItem == null) {
            showError("No Selection", "Please select an item to restore.");
            return;
        }

        if (selectedItem instanceof Deck deck) {
            showConfirmation("Restore Deck", "Are you sure you want to restore this deck?",
                    "This action will make the deck and its cards visible again.", response -> {
                        if (response == ButtonType.OK) {
                            deckDAO.restoreDeck(deck);
                            cardDAO.restoreCardsByDeck(deck);
                            reloadList();
                            logger.info("A deck has been restored");
                        }
                    });
        } else if (selectedItem instanceof Card card) {
            showConfirmation("Restore Flashcard", "Are you sure you want to restore this card?",
                    "This action will make the card visible in its deck again.", response -> {
                        if (response == ButtonType.OK) {
                            cardDAO.restoreCard(card);
                            reloadList();
                            logger.info("A card has been restored");
                        }
                    });
        }
    }

    /**
     * Permanently deletes all items in the recycle bin.
     */
    @FXML
    private void deleteAll() {
        if (recycleBinList.getItems().isEmpty()) {
            showError("Empty Recycle Bin", "The recycle bin is already empty.");
            return;
        }

        showConfirmation("Delete All Items", "Are you sure you want to permanently delete all items?",
                "This action will delete all items in the recycle bin forever.", response -> {
                    if (response == ButtonType.OK) {
                        User currentUser = ApplicationState.getCurrentUser();
                        List<Deck> deletedDecks = deckDAO.getSoftDeletedDecks(currentUser);
                        List<Card> deletedCards = getSoftDeletedCards(currentUser);

                        for (Deck deck : deletedDecks) {
                            deckDAO.deleteDeck(deck);
                            cardDAO.deleteCardsByDeck(deck);
                        }

                        for (Card card : deletedCards) {
                            cardDAO.deleteCard(card);
                        }

                        reloadList();
                        logger.info("Recycle bin is empty");
                    }
                });
    }

    /**
     * Restores all items in the recycle bin.
     */
    @FXML
    private void restoreAll() {
        if (recycleBinList.getItems().isEmpty()) {
            showError("Empty Recycle Bin", "There are no items to restore.");
            return;
        }

        showConfirmation("Restore All Items", "Are you sure you want to restore all items?",
                "This action will restore all items in the recycle bin.", response -> {
                    if (response == ButtonType.OK) {
                        User currentUser = ApplicationState.getCurrentUser();
                        List<Deck> deletedDecks = deckDAO.getSoftDeletedDecks(currentUser);
                        List<Card> deletedCards = getSoftDeletedCards(currentUser);

                        for (Deck deck : deletedDecks) {
                            deckDAO.restoreDeck(deck);
                            cardDAO.restoreCardsByDeck(deck);
                        }

                        for (Card card : deletedCards) {
                            cardDAO.restoreCard(card);
                        }

                        reloadList();
                        logger.info("All items in the recycle bin have been restored");
                    }
                });
    }

    private void reloadList() {
        if (!ApplicationState.isUserLoggedIn()) return;
        User currentUser = ApplicationState.getCurrentUser();
        recycleBinList.getItems().clear();
        recycleBinList.getItems().addAll(deckDAO.getSoftDeletedDecks(currentUser));
        recycleBinList.getItems().addAll(getSoftDeletedCards(currentUser));
        recycleBinList.refresh();
    }

    /**
     * Gets all soft-deleted cards for a given user.
     * This method retrieves all non-deleted decks for the user, and then for each deck,
     * finds all cards that have been soft-deleted.
     *
     * @param user the user to get soft-deleted cards for
     * @return a list of soft-deleted cards
     */
    private List<Card> getSoftDeletedCards(User user) {
        List<Card> softDeletedCards = new ArrayList<>();
        if (user == null || user.getId() == 0) return softDeletedCards;

        try {
            // Get all non-deleted decks for the user
            List<Deck> decks = deckDAO.getDecks(user);

            // For each deck, get all soft-deleted cards using the DAO
            for (Deck deck : decks) {
                List<Card> deletedCardsForDeck = cardDAO.getSoftDeletedCardsForDeck(deck);
                softDeletedCards.addAll(deletedCardsForDeck);
            }
        } catch (Exception e) {
            // Log the error if needed
            System.err.println("Failed to get soft-deleted cards: " + e.getMessage());
        }

        return softDeletedCards;
    }

    private void showConfirmation(String title, String header, String content, java.util.function.Consumer<ButtonType> action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(action);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
