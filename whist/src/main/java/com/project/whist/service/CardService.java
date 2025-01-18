package com.project.whist.service;

import com.project.whist.dto.RoundStart;
import com.project.whist.dto.request.BidDto;
import com.project.whist.dto.request.CardDto;
import com.project.whist.model.*;
import com.project.whist.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.project.whist.util.RoundMappingUtil.getRoundDetails;

@Service
@RequiredArgsConstructor
public class CardService {
    private static List<Card> deck;

    private static final List<String> VALUES = Arrays.asList("7", "8", "9", "10", "J", "Q", "K", "A");

    private final CardRepository cardRepository;
    private final GameSessionRepository gameSessionRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final RoundRepository roundRepository;
    private final RoundMoveRepository roundMoveRepository;

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

    protected static RoundStart dealHand(int handSize, int playerCount) {

        List<Card> shuffledDeck = shuffledDeck();

        if (shuffledDeck.size() < handSize) {
            throw new IllegalArgumentException("Deck size must be at least the size of the hand.");
        }

        List<List<Card>> hands = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            List<Card> hand = new ArrayList<>();
            for (int j = 0; j < handSize; j++) {
                hand.add(shuffledDeck.removeFirst());
            }
            hands.add(hand);
        }

        return new RoundStart(hands, shuffledDeck.getFirst());
    }

    private static int compareValues(String value1, String value2) {
        return Integer.compare(VALUES.indexOf(value1), VALUES.indexOf(value2));
    }

    public CardDto playCard(String username, String gameCode, CardDto cardDto) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        Optional<GameSessionPlayer> gameSessionPlayerOptional = gameSessionPlayerRepository.findGameSessionPlayerByGameSessionIdAndUsername(gameSession.getId(), username);
        GameSessionPlayer gameSessionPlayer = gameSessionPlayerOptional.orElseThrow();

        Integer roundNumber = gameSession.getCurrentRound();
        List<Integer> roundMap = getRoundDetails(roundNumber);
        Round round = roundRepository.findByGameSessionIdAndRoundNumber(gameSession.getId(), roundMap.get(2));

        Card card = cardRepository.findByValueAndSuit(cardDto.value(), cardDto.suit()).orElseThrow();

        RoundMove roundMove = RoundMove.builder()
                .round(round)
                .gameSessionPlayer(gameSessionPlayer)
                .cardPlayed(card)
                .trickWinner(null)
                .build();

        if (round.getMoves() == null) {
            round.setMoves(new ArrayList<>());
        }
        round.getMoves().add(roundMove);

        roundRepository.save(round);

        roundMoveRepository.save(roundMove);

        return new CardDto(roundMove.getCardPlayed().getValue(), roundMove.getCardPlayed().getSuit());
    }
}
