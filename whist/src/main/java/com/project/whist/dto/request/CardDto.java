package com.project.whist.dto.request;

import com.project.whist.model.Card;
import com.project.whist.model.RoundMove;

import java.util.List;

public record CardDto(String value, String suit, Boolean isValid) {

    public static List<CardDto> fromEntityListWithValidation(List<Card> cards, CardDto leadCard, CardDto trumpCard) {

        boolean hasLeadSuit = false;
        boolean hasTrumpSuit = false;

        switch ((leadCard != null ? 1 : 0) + (trumpCard != null ? 2 : 0)) {
            case 0, 2:
                return cards.stream()
                        .map(card -> new CardDto(card.getValue(), card.getSuit(), true))
                        .toList();

            case 1:
                hasLeadSuit = cards.stream().anyMatch(card -> card.getSuit().equals(leadCard.suit()));
                boolean finalHasLeadSuit = hasLeadSuit;
                return cards.stream()
                        .map(card -> new CardDto(
                                card.getValue(),
                                card.getSuit(),
                                card.getSuit().equals(leadCard.suit()) || !finalHasLeadSuit))
                        .toList();


            case 3:
                hasLeadSuit = cards.stream().anyMatch(card -> card.getSuit().equals(leadCard.suit()));
                hasTrumpSuit = cards.stream().anyMatch(card -> card.getSuit().equals(trumpCard.suit()));
                boolean finalHasTrumpSuit1 = hasTrumpSuit;
                boolean finalHasLeadSuit1 = hasLeadSuit;
                return cards.stream()
                        .map(card -> new CardDto(
                                card.getValue(),
                                card.getSuit(),
                                (card.getSuit().equals(leadCard.suit()) || !finalHasLeadSuit1) &&
                                        (card.getSuit().equals(trumpCard.suit()) || !finalHasTrumpSuit1)))
                        .toList();

            default:
                throw new IllegalStateException("Unexpected case in switch statement.");
        }
    }

    public static List<CardDto> fromRoundMoveList(List<RoundMove> moves) {
        return moves.stream().map(move -> new CardDto(move.getCardPlayed().getValue(), move.getCardPlayed().getSuit(), null)).toList();
    }

    public static CardDto fromEntity(Card card) {
        return new CardDto(card.getValue(), card.getSuit(), null);
    }
}
