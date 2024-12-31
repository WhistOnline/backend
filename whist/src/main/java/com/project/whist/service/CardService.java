package com.project.whist.service;

import com.project.whist.model.Card;
import com.project.whist.repository.CardRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CardService {
    private static List<Card> deck;

    private static final List<String> VALUES = Arrays.asList("7", "8", "9", "10", "J", "Q", "K", "A");

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @PostConstruct
    public void initializeDeck() {
        List<Card> cards = cardRepository.findAll();
        deck = cards.stream().toList();
    }

    protected static List<Card> getDeck() {
        return deck;
    }

    protected static List<Card> shuffledDeck() {
        List<Card> shuffledDeck = new ArrayList<>(deck);
        Collections.shuffle(shuffledDeck);
        return shuffledDeck;
    }

    public static Card determineWinner(List<Card> playedCards, Card trumpCard) {
        if (playedCards.size() != 4) {
            throw new IllegalArgumentException("Exactly 4 cards must be provided.");
        }

        Card leadingCard = playedCards.get(0);
        String leadingSuit = leadingCard.getSuit();
        String trumpSuit = (trumpCard != null) ? trumpCard.getSuit() : null;

        Card winner = leadingCard;

        for (Card card : playedCards) {
            if (card.getSuit().equals(trumpSuit)) {
                // Trump suit beats all other suits
                if (!winner.getSuit().equals(trumpSuit) || compareValues(card.getValue(), winner.getValue()) > 0) {
                    winner = card;
                }
            } else if (card.getSuit().equals(leadingSuit)) {
                // Compare values if the suits are the same as the leading suit
                if (compareValues(card.getValue(), winner.getValue()) > 0) {
                    winner = card;
                }
            }
        }

        return winner;
    }

    private static int compareValues(String value1, String value2) {
        return Integer.compare(VALUES.indexOf(value1), VALUES.indexOf(value2));
    }
}
