package dbs;

import dbs.tools.CardDeck52;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class TexasHoldemCombination implements Comparable<TexasHoldemCombination> {
    public enum CombinationType {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfAKind,
        Straight,
        Flush,
        FullHouse,
        FourOfAKind,
        StraightFlush,
        RoyalFlush
    }

    CombinationType combinationType;
    List<CardDeck52.Card> combinationCards;

    // returns the highest flush
    private static List<CardDeck52.Card> getFlush(List<CardDeck52.Card> cards) {
        List<CardDeck52.Card> combination = new ArrayList<>();
        for (CardDeck52.Card.Sign sign : CardDeck52.Card.Sign.values()) {
            for (int startIndex = cards.size() - 5; startIndex >= 0; startIndex--) {
                for (int i = startIndex; i < cards.size(); i++) {
                    if (cards.get(i).sign == sign) combination.add(cards.get(i));
                    if (combination.size() == 5) return combination;
                }

                combination.clear();
            }
        }

        return null;
    }

    // returns a list of straights, ordered from high value to low value
    private static List<List<CardDeck52.Card>> getStraights(List<CardDeck52.Card> cards) {
        List<List<CardDeck52.Card>> combinations = new ArrayList<>();
        for (int startIndex = cards.size() - 5; startIndex >= 0; startIndex--) {
            List<CardDeck52.Card> combination = new ArrayList<>();
            for (int i = startIndex; i < cards.size(); i++) {
                CardDeck52.Card next = cards.get(i);
                if (combination.size() == 0 || next.value - combination.get(combination.size() - 1).value == 1) {
                    combination.add(next);
                }
                if (combination.size() == 5) {
                    combinations.add(combination);
                    break;
                }
            }
        }

        return combinations;
    }

    // returns the highest tuple
    private static List<CardDeck52.Card> getSameValue(List<CardDeck52.Card> cards, int count) {
        List<CardDeck52.Card> combination = new ArrayList<>();
        for (int startIndex = cards.size() - 1; startIndex >= count - 1; startIndex--) {
            for (int i = startIndex; i >= 0; i--) {
                if (combination.size() == 0 || cards.get(i).value == combination.get(0).value) {
                    combination.add(cards.get(i));
                }
                if (combination.size() == count) return combination;
            }

            combination.clear();
        }

        return null;
    }

    // a)
    TexasHoldemCombination(List<CardDeck52.Card> tableCards, TexasHoldemHand hand) {
        this.combinationCards = new ArrayList<>();

        List<CardDeck52.Card> cards = new ArrayList<>(tableCards);
        if (hand.card1 != null)
            cards.add(hand.card1);
        if (hand.card2 != null)
            cards.add(hand.card2);
        cards.sort(Comparator.naturalOrder());

        List<List<CardDeck52.Card>> straights = getStraights(cards);

        // royal/straight flush
        for (List<CardDeck52.Card> straight : straights) {
            List<CardDeck52.Card> flush = getFlush(straight);
            if (flush != null) {
                if (flush.get(flush.size() - 1).isAce())
                    combinationType = CombinationType.RoyalFlush;
                else
                    combinationType = CombinationType.StraightFlush;
                combinationCards = straight;
                return;
            }
        }

        // quad
        List<CardDeck52.Card> quad = getSameValue(cards, 4);
        if (quad != null) {
            combinationType = CombinationType.FourOfAKind;
            combinationCards = quad;
            return;
        }

        // full house/trips
        List<CardDeck52.Card> trips = getSameValue(cards, 3);
        if (trips != null) {
            List<CardDeck52.Card> cardsWithoutTrips = new ArrayList<>(cards);
            cardsWithoutTrips.removeAll(trips);
            List<CardDeck52.Card> pair = getSameValue(cardsWithoutTrips, 2);
            if (pair != null) {
                // full house
                combinationType = CombinationType.FullHouse;
                trips.addAll(pair);
                combinationCards = trips;
            } else {
                // trips
                combinationType = CombinationType.ThreeOfAKind;
                combinationCards = trips;
            }

            return;
        }

        // flush
        List<CardDeck52.Card> flush = getFlush(cards);
        if (flush != null) {
            combinationType = CombinationType.Flush;
            combinationCards = flush;
            return;
        }

        // straight (check that colors differ)
        if (straights.size() > 0) {
            List<CardDeck52.Card> straight = straights.get(0);
            combinationType = CombinationType.Straight;
            combinationCards = straight;
            return;
        }


        // two pair/ one pair
        List<CardDeck52.Card> pair1 = getSameValue(cards, 2);
        if (pair1 != null) {
            List<CardDeck52.Card> cardsWithoutPair1 = new ArrayList<>(cards);
            cardsWithoutPair1.removeAll(pair1);
            List<CardDeck52.Card> pair2 = getSameValue(cardsWithoutPair1, 2);
            if (pair2 != null) {
                // two pair
                combinationType = CombinationType.TwoPair;
                pair2.addAll(pair1);
                combinationCards = pair2;
            } else {
                // one pair
                combinationType = CombinationType.OnePair;
                combinationCards = pair1;
            }

            return;
        }

        // high card
        CardDeck52.Card highest = cards.get(0);
        for (CardDeck52.Card card : cards) {
            if (card.value > highest.value) {
                highest = card;
            }
        }

        cards.clear();
        cards.add(highest);
        combinationType = CombinationType.HighCard;
        combinationCards = cards;
    }

    // b)
    @Override
    public final int compareTo(TexasHoldemCombination that) {
        int typeCompare = combinationType.compareTo(that.combinationType);
        if (typeCompare > 0) return 1;
        else if (typeCompare < 0) return -1;

        // equal combinations; compare on a case-by-case basis
        // all of the rules are properly applied by just iterating through all cards and comparing them individually
        // this works because all of the cards are guaranteed to be sorted by their value
        for (int i = 0; i < combinationCards.size() && i < 5; i++) {
            int cmp = combinationCards.get(i).compareTo(that.combinationCards.get(i));
            if (cmp > 0) return 1;
            else if (cmp < 0) return -1;
        }

        return 0;
    }

    // c)
    public static Stream<TexasHoldemCombination> generate(boolean randomize) {
        Iterator texasHoldemIterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                CardDeck52 deck = new CardDeck52();
                List<CardDeck52.Card> tableCards;
                if (randomize) {
                    boolean hasTable = Math.random() >= 0.5;
                    tableCards = hasTable ?
                            deck.deal(ThreadLocalRandom.current().nextInt(3, 5 + 1)) :
                            Collections.emptyList();
                } else {
                    tableCards = deck.deal(5);
                }
                TexasHoldemHand hand = new TexasHoldemHand();
                hand.takeDeal(deck.deal());
                hand.takeDeal(deck.deal());
                return new TexasHoldemCombination(tableCards, hand);
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(texasHoldemIterator, Spliterator.ORDERED), false);
    }

    public static void main(String[] args) {
        int sampleSize = 1000000;

        Map<CombinationType, List<TexasHoldemCombination>> combinations = generate(false).limit(sampleSize).collect(Collectors.groupingBy(combination -> combination.combinationType));
        SortedMap<CombinationType, Integer> sortedCombinations = new TreeMap<>();
        combinations.forEach((key, val) -> sortedCombinations.put(key, val.size()));
        for (CombinationType type : sortedCombinations.keySet()) {
            int count = combinations.get(type).size();
            System.out.printf("%s: %d (%f%%)\n", type, count, count / (float) sampleSize * 100.f);
        }
    }
}
