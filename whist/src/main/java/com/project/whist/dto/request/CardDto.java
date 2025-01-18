package com.project.whist.dto.request;

import com.project.whist.model.Card;
import com.project.whist.model.RoundMove;

import java.util.List;

public record CardDto(String value, String suit) {

    public static List<CardDto> fromEntityList(List<Card> cards) {
        return cards.stream().map(card -> new CardDto(card.getValue(), card.getSuit())).toList();
    }

    public static List<CardDto> fromRoundMoveList(List<RoundMove> moves) {
        return moves.stream().map(move -> new CardDto(move.getCardPlayed().getValue(), move.getCardPlayed().getSuit())).toList();
    }

    public static CardDto fromEntity(Card card) {
        return new CardDto(card.getValue(), card.getSuit());
    }
}
