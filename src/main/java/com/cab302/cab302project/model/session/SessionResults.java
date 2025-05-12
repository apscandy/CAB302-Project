package com.cab302.cab302project.model.session;

import com.cab302.cab302project.model.card.Card;

import java.time.Duration;
import java.time.LocalDateTime;

public final class SessionResults {


    private final Session session;
    private final Card card;
    private Boolean correct = false;
    private Boolean incorrect = false;
    private long timeToAnswer = 0;

    public SessionResults(Session session, Card card) {
        this.session = session;
        this.card = card;
    }

    public int getId() {
        return session.getId();
    }

    public int getCardId() {
        return card.getId();
    }

    public Boolean getCorrect() {
        return correct;
    }

    void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Boolean getIncorrect() {
        return incorrect;
    }

    void setIncorrect(boolean incorrect) {
        this.incorrect = incorrect;
    }


    public void setTimeToAnswer(LocalDateTime startTime, LocalDateTime endTime) {
        this.timeToAnswer = Duration.between(startTime, endTime).toSeconds();
    }

    public String getTimeToAnswer() {
        return String.format("%d", timeToAnswer);
    }



}
