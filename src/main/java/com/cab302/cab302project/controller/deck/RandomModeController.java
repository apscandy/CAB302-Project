package com.cab302.cab302project.controller.deck;


import com.cab302.cab302project.model.card.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RandomModeController  implements Initializable {

    @FXML private TextArea questionTextArea;
    @FXML private TextArea answerTextArea;

    @FXML private ListView<Card> cards;
    private List<Card> currentshuffledCards;
    private int index = 0;

    public void setShuffledCards(List<Card> shuffledCards) {
        this.currentshuffledCards = shuffledCards;
        if (shuffledCards != null && !shuffledCards.isEmpty()) {
            showCardAt(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentshuffledCards != null && !currentshuffledCards.isEmpty()) {
            showCardAt(index);
        }
    }

    private void showCardAt(int i) {
        Card c = currentshuffledCards.get(i);
        questionTextArea.setText(c.getQuestion());
        answerTextArea.setText(c.getAnswer());
    }

    @FXML
    private void handleNextButton() {
        if (currentshuffledCards == null || currentshuffledCards.isEmpty()) return;

        if (index >= currentshuffledCards.size() - 1) {
            // Đã tới câu cuối
            showAlert("Section Finished", "You have reached the end of the session.");
        } else {
            index++;
            showCardAt(index);
        }
    }

    @FXML
    private void handleBackButton() {
        if (currentshuffledCards == null || currentshuffledCards.isEmpty()) return;
        index = (index - 1 + currentshuffledCards.size()) % currentshuffledCards.size();
        showCardAt(index);
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
