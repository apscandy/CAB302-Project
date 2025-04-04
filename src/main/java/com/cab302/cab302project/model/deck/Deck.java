package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.user.User;

import java.util.List;


public class Deck {
    private int id;
    private String name;
    private String description;
    private final User user;
    private List<Card> cards;


    public Deck(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Deck(String name, String description, User user, List<Card> cards) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return name;
    }
}
