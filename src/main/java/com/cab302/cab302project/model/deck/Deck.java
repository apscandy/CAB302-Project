package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.user.User;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class Deck {
    private int id;
    private String name;
    private String description;
    private Boolean isBookmarked = false;
    private final User user;
    private List<Card> cards;


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public Deck(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public Deck(String name, String description, User user, List<Card> cards) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.cards = cards;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public int getId() {
        return id;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    protected void setId(int id) {
        this.id = id;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public int getUserId() {
        return user.getId();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public String getName() {
        return name;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public String getDescription() {
        return description;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public String toString() {
        return name;
    }
}
