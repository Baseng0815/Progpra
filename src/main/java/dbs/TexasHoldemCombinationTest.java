package dbs;

import dbs.tools.CardDeck52;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TexasHoldemCombinationTest {
    @Test
    public void testA() {
        TexasHoldemHand emptyHand = new TexasHoldemHand();
        List<CardDeck52.Card> cards = new ArrayList<>();
        // royal flush
        cards.add(new CardDeck52.Card(14, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(5, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(12, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(13, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Clubs));

        TexasHoldemCombination combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.RoyalFlush, combination.combinationType);
        cards.clear();

        // straight flush
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(13, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(4, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.StraightFlush, combination.combinationType);
        cards.clear();

        // quad
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(4, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Hearts));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.FourOfAKind, combination.combinationType);
        cards.clear();

        // full house
        cards.add(new CardDeck52.Card(6, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(6, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(6, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.FullHouse, combination.combinationType);
        cards.clear();

        // flush
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(4, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(13, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.Flush, combination.combinationType);
        cards.clear();

        // straight
        cards.add(new CardDeck52.Card(3, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(4, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(13, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(5, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(6, CardDeck52.Card.Sign.Spades));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.Straight, combination.combinationType);
        cards.clear();

        // trips
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(2, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Clubs));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.ThreeOfAKind, combination.combinationType);
        cards.clear();

        // two pairs
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(12, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(8, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.TwoPair, combination.combinationType);
        cards.clear();

        // one pair
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(9, CardDeck52.Card.Sign.Hearts));
        cards.add(new CardDeck52.Card(13, CardDeck52.Card.Sign.Spades));
        cards.add(new CardDeck52.Card(5, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(10, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.OnePair, combination.combinationType);
        cards.clear();

        // high card
        cards.add(new CardDeck52.Card(11, CardDeck52.Card.Sign.Diamonds));
        cards.add(new CardDeck52.Card(7, CardDeck52.Card.Sign.Clubs));
        cards.add(new CardDeck52.Card(14, CardDeck52.Card.Sign.Diamonds));

        combination = new TexasHoldemCombination(cards, emptyHand);
        Assertions.assertEquals(TexasHoldemCombination.CombinationType.HighCard, combination.combinationType);
        cards.clear();
    }

    @Test
    public void testB() {
        int count = 50;

        TexasHoldemCombination.generate(true)
                .limit(count)
                .sorted(TexasHoldemCombination::compareTo)
                .forEach(tc1 -> System.out.printf("Comparing %s (%s)\n", tc1.combinationType, tc1.combinationCards));
    }
}