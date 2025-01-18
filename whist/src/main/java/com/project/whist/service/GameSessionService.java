package com.project.whist.service;

import com.project.whist.dto.request.CardDto;
import com.project.whist.dto.request.RoundPlayDto;
import com.project.whist.dto.request.JoinGameSessionDto;
import com.project.whist.dto.request.UserCardHandDto;
import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.GameSessionResponseDto;
import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.RoundResultDto;
import com.project.whist.model.*;
import com.project.whist.repository.GameSessionPlayerRepository;
import com.project.whist.repository.GameSessionRepository;
import com.project.whist.repository.RoundRepository;
import com.project.whist.repository.UserRepository;
import com.project.whist.util.GameCodeGeneratorUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.whist.util.GameCodeGeneratorUtil.generateGameCode;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final RoundRepository roundRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;

    @Transactional
    public GameSessionResponseDto createGameSession(final String username) {
        GameSession gameSession = new GameSession();
        GameSessionPlayer gameSessionPlayer = gameSessionPlayerRepository.findByUsername(username);
        if (gameSessionPlayer == null) {
            gameSessionPlayer = new GameSessionPlayer();
            User user = userRepository.findUserByUsername(username);
            gameSessionPlayer.setUser(user);
        }
        gameSession.setPlayers(new ArrayList<>(List.of(gameSessionPlayer)));
        gameSession.setStatus("joining");
        gameSession.setMaxPlayers(4);
        gameSession.setCreatedAt(new Date().toInstant());
        gameSession.setActive(true);
        gameSession.setGameCode(generateGameCode());
        gameSession = gameSessionRepository.save(gameSession);
        gameSessionPlayer.setGameSession(gameSession);
        gameSessionPlayer.setIsDealer(true);
        gameSessionPlayerRepository.save(gameSessionPlayer);

        return new GameSessionResponseDto(gameSession.getId(), gameSession.getStatus(), gameSession.getMaxPlayers(), gameSession.getCreatedAt(), gameSession.getGameCode());
    }

    @Transactional
    public GameSessionResponseDto joinGameSession(final String username, final String gameCode) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        GameSessionPlayer gameSessionPlayer = gameSessionPlayerRepository.findByUsername(username);
        if (gameSessionPlayer == null) {
            gameSessionPlayer = new GameSessionPlayer();
            User user = userRepository.findUserByUsername(username);
            gameSessionPlayer.setUser(user);
        }
        gameSessionPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(gameSessionPlayer);
        gameSessionRepository.save(gameSession);

        return new GameSessionResponseDto(gameSession.getId(), gameSession.getStatus(), gameSession.getMaxPlayers(), gameSession.getCreatedAt(), gameSession.getGameCode());
    }

    public GameStateDto retrieveGame(final String username, final String gameCode) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();

        if (gameSession.getCurrentRound() == 0) {
            gameSession.setCurrentRound(1);
            gameSessionRepository.save(gameSession);
        } else {
            gameSession.setCurrentRound(gameSession.getCurrentRound() + 1);
            gameSessionRepository.save(gameSession);
        }

        GameSessionPlayer gameSessionPlayer = gameSessionPlayerRepository.findByUsername(username);

        UserCardHandDto hand = new UserCardHandDto(username, new ArrayList<>(), true);
        List<Card> cardsPlayed = new ArrayList<>();
        Card trumpCard = null;
        Map<String, Map<Integer, Integer>> scoreBoard = new HashMap<>();

        return new GameStateDto(hand, cardsPlayed, trumpCard, scoreBoard);
    }

//    public List<RoundResultDto> playRound(final Long gameSessionId, final RoundPlayDto roundPlayDto) {
//        GameSession gameSession = gameSessionRepository.findById(gameSessionId).orElseThrow();
//        Round round = new Round();
//        round.setGameSession(gameSession);
//        round.setRoundNumber(roundPlayDto.roundNo());
//        round.setTrumpSuit(roundPlayDto.trumpSuit());
//        if (roundPlayDto.trumpSuit() != null) {
//            round.setTrumpSuit(roundPlayDto.trumpSuit());
//
//        }
//        List<RoundResultDto> result = new ArrayList<>();
//        if (roundPlayDto.roundNo() <= 10 || roundPlayDto.roundNo() >= 15) {
//            round.setType("noTrump");
//        } else {
//            round.setType("trump");
//        }
//        Map<String, Integer> resultMap = findRoundOfOneWinner(roundPlayDto.hands(), roundPlayDto.trumpSuit());
//        resultMap.forEach((username, resultValue) -> {
//            result.add(new RoundResultDto(username, resultValue));
//        });
//        User trickWinner = userRepository.findUserByUsername(resultMap.entrySet().stream()
//                .filter(entry -> entry.getValue() == 1)
//                .findFirst()
//                .orElseThrow()
//                .getKey());
//        roundRepository.save(round);
//        roundPlayDto.hands().forEach(hand -> {
//            RoundMove roundMove = new RoundMove();
//            roundMove.setRound(round);
//            roundMove.setUser(userRepository.findUserByUsername(hand.username()));
//            roundMove.setCardPlayed(hand.card().value());
//            roundMove.setMoveOrder(hand.order());
//            roundMove.setTrickWinner(trickWinner);
//            if (round.getMoves() == null) {
//                round.setMoves(new ArrayList<>(List.of(roundMove)));
//            } else {
//                round.getMoves().add(roundMove);
//            }
//        });
//        return result;
//    }
//
//    private Map<String, Integer> findRoundOfOneWinner(final List<UserCardHandDto> hands, final String trumpSuit) {
//
//        String leadingSuit = hands.stream()
//                .filter(hand -> hand.order() == 1)
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("No hand with order 1 found"))
//                .card().suit();
//
//        UserCardHandDto winnerHand = hands.stream()
//                .max((hand1, hand2) -> {
//
//                    CardDto card1 = hand1.card();
//                    CardDto card2 = hand2.card();
//
//                    boolean isCard1Trump = card1.suit().equals(trumpSuit);
//                    boolean isCard2Trump = card2.suit().equals(trumpSuit);
//
//                    if (isCard1Trump && !isCard2Trump) return 1;
//                    if (!isCard1Trump && isCard2Trump) return -1;
//
//                    boolean isCard1Leading = card1.suit().equals(leadingSuit);
//                    boolean isCard2Leading = card2.suit().equals(leadingSuit);
//
//                    if (isCard1Leading && !isCard2Leading) return 1;
//                    if (!isCard1Leading && isCard2Leading) return -1;
//
//                    return cardValueComparator(card1.value(), card2.value());
//                })
//                .orElseThrow(() -> new IllegalArgumentException("Cannot determine the winner"));
//
//        Map<String, Integer> result = new HashMap<>();
//        hands.forEach(hand -> result.put(hand.username(), 0));
//
//        result.put(winnerHand.username(), 1);
//        return result;
//    }
//
//    private int cardValueComparator(String card1, String card2) {
//        List<String> cardOrder = List.of("7", "8", "9", "10", "J", "Q", "K", "A");
//        return Integer.compare(cardOrder.indexOf(card1), cardOrder.indexOf(card2));
//    }
}
