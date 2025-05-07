package com.cab302.cab302project.util;

import com.cab302.cab302project.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomModeUtils {

    private static final Random random = new Random();

    /**
     * Returns a new list of cards randomly shuffled from the original deck.
     *
     * @param originalCards The original list of cards from the deck.
     * @return A new shuffled list of cards.
     */
    public static List<Card> getRandomizedCardList(List<Card> originalCards) {
        List<Card> shuffled = new ArrayList<>(originalCards); // Create a copy
        for (int i = shuffled.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(shuffled, i, j);
        }
        return shuffled;
    }

    /**
     * Prints a list of shuffled cards to the terminal for debug or test.
     */
    public static List<Card> getShuffledCards(List<Card> cards) {
        List<Card> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        return shuffled;
    }
}

