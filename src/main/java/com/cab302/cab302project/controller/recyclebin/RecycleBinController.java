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
import com.cab302.cab302project.util.ShowAlertUtils;
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
 * Controller for the Recycle Bin view management.
 * <p>
 * Provides handlers for displaying, permanently deleting, and restoring
 * soft-deleted decks and cards. Manages the recycle bin interface where users
 * can view all their deleted items and choose to either restore them back to
 * their active state or permanently delete them from the database. All operations
 * require user confirmation and respect the current user's authentication state.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
 **/
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

    /**
     * Initializes the recycle bin view components.
     * <p>
     * Sets up the ListView cell factory to properly display both Deck and Card
     * objects with appropriate string representations. For decks, shows the deck name.
     * For cards, shows the format "[DeckName] CardQuestion". Loads all soft-deleted
     * items for the current user.
     * </p>
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     * @author Hoang Dat Bui (n11659831)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!ApplicationState.isUserLoggedIn()) return;
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

    /**
     * Handles item selection in the recycle bin list.
     * <p>
     * Captures the currently selected item from the ListView and stores it
     * in the selectedItem field for use by other operations. Returns early
     * if no user is logged in. Logs the selection action.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
    @FXML
    private void selectItem() {
        if (!ApplicationState.isUserLoggedIn()) return;
        selectedItem = recycleBinList.getSelectionModel().getSelectedItem();
        logger.info("An item is selected");
    }

    /**
     * Permanently deletes the selected item from the database.
     * <p>
     * Validates that an item is selected, then displays a confirmation dialog
     * based on the item type (Deck or Card). If confirmed, calls the appropriate
     * DAO method to permanently remove the item from the database. For decks,
     * this also deletes all associated cards.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
    @FXML
    private void deleteAnItem() {
        if (selectedItem == null) {
            ShowAlertUtils.showError("No Selection", "Please select an item to delete.");
            return;
        }

        if (selectedItem instanceof Deck deck) {
            ShowAlertUtils.showConfirmation("Delete Deck", "Are you sure you want to permanently delete this deck?",
                    "This action will delete the deck and all its cards forever.", response -> {
                        if (response == ButtonType.OK) {
                            deckDAO.deleteDeck(deck);
                            reloadList();
                            logger.info("A deck has been deleted");
                        }
                    });
        } else if (selectedItem instanceof Card card) {
            ShowAlertUtils.showConfirmation("Delete Card", "Are you sure you want to permanently delete this card?",
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
     * <p>
     * Validates that an item is selected, then displays a confirmation dialog
     * based on the item type. If confirmed, calls the DAO restore
     * method to make the item visible again. For decks, also restores all
     * associated cards. For cards, only restores the individual card.
     * Refreshes the list after restoration and logs the action.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
    @FXML
    private void restoreAnItem() {
        if (selectedItem == null) {
            ShowAlertUtils.showError("No Selection", "Please select an item to restore.");
            return;
        }

        if (selectedItem instanceof Deck deck) {
            ShowAlertUtils.showConfirmation("Restore Deck", "Are you sure you want to restore this deck?",
                    "This action will make the deck and its cards visible again.", response -> {
                        if (response == ButtonType.OK) {
                            deckDAO.restoreDeck(deck);
                            cardDAO.restoreCardsByDeck(deck);
                            reloadList();
                            logger.info("A deck has been restored");
                        }
                    });
        } else if (selectedItem instanceof Card card) {
            ShowAlertUtils.showConfirmation("Restore Flashcard", "Are you sure you want to restore this card?",
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
     * <p>
     * Checks if the recycle bin is empty and shows an error if so. Otherwise,
     * displays a confirmation dialog warning about the permanent nature of the
     * operation. If confirmed, retrieves all soft-deleted decks and cards for
     * the current user and permanently deletes them from the database.
     * Refreshes the list after completion and logs the action.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
    @FXML
    private void deleteAll() {
        if (recycleBinList.getItems().isEmpty()) {
            ShowAlertUtils.showError("Empty Recycle Bin", "The recycle bin is already empty.");
            return;
        }

        ShowAlertUtils.showConfirmation("Delete All Items", "Are you sure you want to permanently delete all items?",
                "This action will delete all items in the recycle bin forever.", response -> {
                    if (response == ButtonType.OK) {
                        User currentUser = ApplicationState.getCurrentUser();
                        List<Deck> deletedDecks = deckDAO.getSoftDeletedDecks(currentUser);
                        List<Card> deletedCards = getSoftDeletedCards(currentUser);

                        for (Deck deck : deletedDecks) {
                            deckDAO.deleteDeck(deck);
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
     * <p>
     * Checks if there are items to restore and shows an error if the bin is empty.
     * Otherwise, displays a confirmation dialog. If confirmed, retrieves all
     * soft-deleted decks and cards for the current user and restores them to
     * their active state. For decks, also restores all associated cards.
     * Refreshes the list after completion and logs the action.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
    @FXML
    private void restoreAll() {
        if (recycleBinList.getItems().isEmpty()) {
            ShowAlertUtils.showError("Empty Recycle Bin", "There are no items to restore.");
            return;
        }

        ShowAlertUtils.showConfirmation("Restore All Items", "Are you sure you want to restore all items?",
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

    /**
     * Reloads the recycle bin list with current soft-deleted items.
     * <p>
     * Clears the current ListView contents and repopulates it with all
     * soft-deleted decks and cards for the logged-in user. Returns early
     * if no user is logged in. Refreshes the ListView display after
     * adding all items.
     * </p>
     * @author Hoang Dat Bui (n11659831)
     */
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
     * <p>
     * This method retrieves all non-deleted decks for the user, and then for each deck,
     * finds all cards that have been soft-deleted. Handles null users and users with
     * invalid IDs by returning an empty list. Catches and logs any exceptions that
     * occur during the database operations, ensuring the method doesn't crash the
     * application if database errors occur.
     * </p>
     *
     * @param user the user to get soft-deleted cards for
     * @return a list of soft-deleted cards, empty if user is invalid or no cards found
     * @author Hoang Dat Bui (n11659831)
     */
    private List<Card> getSoftDeletedCards(User user) {
        List<Card> softDeletedCards = new ArrayList<>();
        if (user == null || user.getId() == 0) return softDeletedCards;

        try {
            List<Deck> decks = deckDAO.getDecks(user);

            for (Deck deck : decks) {
                List<Card> deletedCardsForDeck = cardDAO.getSoftDeletedCardsForDeck(deck);
                softDeletedCards.addAll(deletedCardsForDeck);
            }
        } catch (Exception e) {
            System.err.println("Failed to get soft-deleted cards: " + e.getMessage());
        }

        return softDeletedCards;
    }

}
