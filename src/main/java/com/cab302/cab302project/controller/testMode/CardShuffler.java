package com.cab302.cab302project.controller.testMode;

import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.session.SqliteSessionResultsDAO;

import java.util.*;

public class CardShuffler {

    private ICardDAO cardDAO;

    public CardShuffler() {
        this.cardDAO = new SqliteCardDAO();
    }

    /**
     * Returns a smart-shuffled list of cards:
     * - Cards with wrong rate >= 0.55 are first, sorted from highest to lowest wrong rate
     * - Remaining cards are shown in random order
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public List<Card> getSmartShuffledCardsForDeck(Deck deck, Map<Integer, double[]> cardResults) {
        List<Card> allCards = cardDAO.getCardsForDeck(deck);
        List<Card> wrongCards = new ArrayList<>();
        Map<Integer, Double> wrongRates = new HashMap<>();
        List<Card> okCards = new ArrayList<>();
        for (Card card : allCards) {
            double[] stats = cardResults.get(card.getId());
            if (stats == null) {
                okCards.add(card);
                continue;
            }
            double correct = stats[0];
            double incorrect = stats[1];
            double total = correct + incorrect;
            if (total == 0) {
                okCards.add(card);
            } else {
                double wrongRate = incorrect / total;
                if (wrongRate >= 0.55) {
                    wrongCards.add(card);
                    wrongRates.put(card.getId(), wrongRate);
                } else {
                    okCards.add(card);
                }
            }
        }

        // Cards with wrong rate > 55% are sorted by wrong rate in desc order
        wrongCards.sort((c1, c2) -> Double.compare(
                wrongRates.getOrDefault(c2.getId(), 0.0),
                wrongRates.getOrDefault(c1.getId(), 0.0)
        ));

        // The rest will be shuffled in a random order
        Collections.shuffle(okCards);

        List<Card> result = new ArrayList<>();
        result.addAll(wrongCards);
        result.addAll(okCards);
        return result;
    }
}
