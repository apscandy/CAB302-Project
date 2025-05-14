package com.cab302.cab302project.controller.testMode;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.HelloApplication;
import com.cab302.cab302project.controller.ResultsController;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.session.ISessionDAO;
import com.cab302.cab302project.model.session.Session;
import com.cab302.cab302project.model.session.SessionResults;
import com.cab302.cab302project.model.session.SqliteSessionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TestModeStandard {

    private static final Logger logger = LogManager.getLogger(TestModeStandard.class);

    @FXML
    private Text questionOrAnswerTitle;

    @FXML
    private Text questionOrAnswerText;


//    @FXML
//    private Button gotItWrongButton;
//
//    @FXML
//    private Button gotItRightButton;
//
//    @FXML
//    private Button flipButton;

    private List<Card> cards;

    private int deckSize;

    private Boolean showAnswer = false;

    private int correctAnswerCount = 0;

    private int wrongAnswerCount = 0;

    private int currentCardIndex = 0;

    private final Session session;

    private final ISessionDAO sessionDAO;

    private LocalDateTime startTime;


    public TestModeStandard() {
        this.session = new Session(ApplicationState.getDeck(), ApplicationState.getCurrentUser());
        sessionDAO = new SqliteSessionDAO();
        sessionDAO.createSession(session);
    }


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
        fetchCards();
        if (deckSize == 0) {
            logger.warn("please set a deck with at least one card before calling initialize");
            return;
        }
        showFirstCard();
        startTime = LocalDateTime.now();
    }

    private void showFirstCard(){
        if (deckSize == 0){
            logger.warn("please set a deck with at least one card before calling initialize");
            return;
        }
        ShowCardQuestion();
    }

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

    private void ShowCardQuestion() {
        try {
            questionOrAnswerTitle.setText("Question");
            var question = cards.get(currentCardIndex).getQuestion();
            questionOrAnswerText.setText(question);
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    private void ShowCardAnswer() {
        try {
            questionOrAnswerTitle.setText("Answer");
            var answer = cards.get(currentCardIndex).getAnswer();
            questionOrAnswerText.setText(answer);
        } catch (Exception e) {
            logger.error(e);
        }
    }

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
                cards = ApplicationState.getDeck().getCards();
                this.deckSize = cards.size();
                break;
        }
    }
}
