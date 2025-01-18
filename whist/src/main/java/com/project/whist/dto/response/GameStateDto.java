package com.project.whist.dto.response;

import com.project.whist.dto.request.UserCardHandDto;
import com.project.whist.model.Card;

import java.util.List;
import java.util.Map;

public record GameStateDto(UserCardHandDto hand, List<Card> cardsPlayed, Card trumpCard, Map<String, Map<Integer, Integer>> scoreBoard) {
}

// GameStateDto retrieveGameState(String username); - method
// GameState Dto:
// UserCardHandDto - username, List<Card> hand, isMyTurn
// List<Card> cardsPlayed -> in the order they were played
// Card trumpCard
// Map<username, Map<roundNo, Bid/Actual>> scoreBoard


