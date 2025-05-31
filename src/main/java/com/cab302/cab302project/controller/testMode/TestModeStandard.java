package com.cab302.cab302project.controller.testMode;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.controller.results.ResultsController;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.session.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for managing the Test Mode interface in the application.
 * Handles card display, user interaction (flip, correct/incorrect answers),
 * session tracking, and navigation to results.
 */
public class TestModeStandard {

    private static final Logger logger = LogManager.getLogger(TestModeStandard.class);

    @FXML
    private Text questionOrAnswerTitle;

    @FXML
    private Text questionOrAnswerText;

    /**
     * List of cards currently being tested in the session.
     */
    private List<Card> cards;


    /**
     * Total number of cards in the current deck.
     */
    private int deckSize;

    /**
     * Flag indicating whether the answer is currently displayed (true) or question (false).
     */
    private Boolean showAnswer = false;

    /**
     * Counter for correct answers provided by the user.
     */
    private int correctAnswerCount = 0;

    /**
     * Counter for incorrect answers provided by the user.
     */
    private int wrongAnswerCount = 0;


    /**
     * Index of the current card being displayed in the test session.
     */
    private int currentCardIndex = 0;

    /**
     * Session object representing the current testing session.
     */
    private Session session;

    /**
     * DAO for interacting with session data stored in the database.
     */
    private final ISessionDAO sessionDAO;

    /**
     * Timestamp when the test session started.
     */
    private LocalDateTime startTime;


    /**
     * Constructor that initializes the session DAO.
     */
    public TestModeStandard() {
        sessionDAO = new SqliteSessionDAO();
    }


    /**
     * Initializes the test mode interface. Sets up the session, fetches cards based on
     * the current mode (sequential/random/smart), and displays the first card.
     *
     * @throws IllegalStateException if deck or user is not set in ApplicationState
     */
    @FXML
    private void initialize() {
        if (ApplicationState.getDeck() == null) {
            logger.warn("Deck is null please set a deck before calling initialize");
            return;
        }
        if (ApplicationState.getCurrentUser() == null) {
            logger.warn("user is null please set a current user before calling initialize");
            return;
        }
        if (ApplicationState.getCurrentMode() == null) {
            logger.warn("please set a current mode before calling initialize");
            return;
        }
        this.session = new Session(ApplicationState.getDeck(), ApplicationState.getCurrentUser());
        sessionDAO.createSession(session);
        fetchCards();
        if (deckSize == 0) {
            logger.warn("please set a deck with at least one card before calling initialize");
            return;
        }
        showFirstCard();
        startTime = LocalDateTime.now();
    }

    /**
     * Displays the first card of the test session. Calls ShowCardQuestion().
     *
     * @throws IllegalStateException if deck has no cards
     */
    private void showFirstCard(){
        if (deckSize == 0){
            logger.warn("please set a deck with at least one card before calling initialize");
            return;
        }
        ShowCardQuestion();
    }

    /**
     * Advances to the next card in the test session. Updates statistics and ends
     * the session when all cards are completed.
     *
     * @throws IllegalStateException if no more cards are available
     */
    private void showNextCard() {
        showAnswer = false;
        if (currentCardIndex == cards.size() - 1) {
            logger.debug("There are no more cards in the deck");
            sessionDAO.endSession(session);
            sendToResults();
            return;
        }
        currentCardIndex++;
        this.startTime = LocalDateTime.now();
        ShowCardQuestion();
    }

    /**
     * Displays the question text of the current card.
     */
    private void ShowCardQuestion() {
        try {
            questionOrAnswerTitle.setText("Question");
            var question = cards.get(currentCardIndex).getQuestion();
            questionOrAnswerText.setText(question);
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    /**
     * Toggles between displaying the question and answer for the current card.
     */
    private void ShowCardAnswer() {
        try {
            questionOrAnswerTitle.setText("Answer");
            var answer = cards.get(currentCardIndex).getAnswer();
            questionOrAnswerText.setText(answer);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Handles user's confirmation that they answered correctly. Updates session results,
     * increments correct answer count, and moves to next card.
     *
     * @throws IllegalStateException if user tries to mark as correct without seeing the answer
     */
    @FXML
    private void onFlipButtonClicked(){
        if (!showAnswer){
            ShowCardAnswer();
        }else{
            ShowCardQuestion();
        }
        showAnswer = !showAnswer;
    }

    @FXML
    private void onGotItRightButtonClicked(){
        // prevent user from pressing `got it right` without seeing the answer
        if (!showAnswer){
            return;
        }
        var rs = new SessionResults(session, cards.get(currentCardIndex));
        sessionDAO.createSessionResult(rs);
        sessionDAO.sessionResultCardCorrect(rs, startTime, LocalDateTime.now());
        correctAnswerCount +=1;
        showNextCard();
    }



    /**
     * Handles user's confirmation that they answered incorrectly. Updates session results,
     * increments wrong answer count, and moves to next card.
     *
     * @throws IllegalStateException if user tries to mark as incorrect without seeing the answer
     */
    @FXML
    private void onGotItWrongButtonClicked(){
        // prevent user from pressing `got it wrong` without seeing the answer
        if (!showAnswer){
            return;
        }
        var rs = new SessionResults(session, cards.get(currentCardIndex));
        sessionDAO.createSessionResult(rs);
        sessionDAO.sessionResultCardIncorrect(rs, startTime, LocalDateTime.now());
        wrongAnswerCount +=1;
        showNextCard();
    }


    /**
     * Navigates to the results screen, passing correct/incorrect answer counts and session data.
     *
     * @throws IOException if there's an error loading the results view
     */
    private void sendToResults() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("results/results-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage primaryStage = (Stage) questionOrAnswerTitle.getScene().getWindow();
            primaryStage.setScene(scene);

            ResultsController controller = fxmlLoader.getController();
            controller.setResults(correctAnswerCount, wrongAnswerCount, session);
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Fetches cards for the current test session based on the selected mode:
     * - SEQUENTIAL: all cards in deck order
     * - RANDOM: shuffled list of all cards
     * - SMART: dynamically ordered based on user performance
     */
    private void fetchCards() {
        if (ApplicationState.getCurrentMode() == null){
            logger.warn("Current mode is null");
            return;
        }
        switch(ApplicationState.getCurrentMode()) {
            case SEQUENTIAL:
                cards = ApplicationState.getDeck().getCards();
                this.deckSize = cards.size();
                break;
            case RANDOM:
                cards = new SqliteCardDAO().getRandomizedCardsForDeck(ApplicationState.getDeck());
                this.deckSize = cards.size();
                break;
            case SMART:
                cards = new SqliteCardDAO().getSmartShuffledCardsForDeck(
                        ApplicationState.getDeck(),
                        new SqliteSessionResultsDAO().getCardResultsForUser(ApplicationState.getDeck().getUserId())
                );
                this.deckSize = cards.size();
                break;
        }
    }
}
