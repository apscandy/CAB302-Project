package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;

public class Card {
    private int id;
    private Deck deck;
    private String question;
    private String answer;
    private String tags;
    private boolean isDeleted = false;

    public Card(Deck deck, String question, String answer, String tags) {
        this.deck = deck;
        this.question = question;
        this.answer = answer;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public Deck getDeck() {
        return deck;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTags() {
        return tags;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    protected void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return question;
    }
}
