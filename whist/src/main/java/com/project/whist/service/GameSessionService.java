package com.project.whist.service;

import com.project.whist.dto.RoundStart;
import com.project.whist.dto.request.CardDto;
import com.project.whist.dto.request.UserCardHandDto;
import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.GameSessionResponseDto;
import com.project.whist.dto.response.ScoreDetailsDto;
import com.project.whist.dto.response.ScoreboardDto;
import com.project.whist.dto.response.UserScoreDto;
import com.project.whist.model.*;
import com.project.whist.repository.BidRepository;
import com.project.whist.repository.GameSessionPlayerRepository;
import com.project.whist.repository.GameSessionRepository;
import com.project.whist.repository.RoundRepository;
import com.project.whist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.project.whist.util.GameCodeGeneratorUtil.generateGameCode;
import static com.project.whist.util.RoundMappingUtil.getRoundDetails;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final RoundRepository roundRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final RoundService roundService;
    private final BidRepository bidRepository;

    @Transactional
    public GameSessionResponseDto createGameSession(final String username) {
        GameSession gameSession = new GameSession();
        GameSessionPlayer gameSessionPlayer = new GameSessionPlayer();

        User user = userRepository.findUserByUsername(username);
        gameSessionPlayer.setUser(user);

        gameSession.setPlayers(new ArrayList<>(List.of(gameSessionPlayer)));
        gameSession.setMoveOrder(0);
        gameSession.setStatus("joining");
        gameSession.setMaxPlayers(4);
        gameSession.setCreatedAt(new Date().toInstant());
        gameSession.setActive(true);
        gameSession.setGameCode(generateGameCode());
        gameSession = gameSessionRepository.save(gameSession);

        gameSessionPlayer.setGameSession(gameSession);
        gameSessionPlayer.setIsTurn(true);
        gameSessionPlayerRepository.save(gameSessionPlayer);

        return new GameSessionResponseDto(gameSession.getId(), gameSession.getStatus(), gameSession.getMaxPlayers(), gameSession.getCreatedAt(), gameSession.getGameCode());
    }

    @Transactional
    public GameSessionResponseDto joinGameSession(final String username, final String gameCode) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        GameSessionPlayer gameSessionPlayer = new GameSessionPlayer();
        User user = userRepository.findUserByUsername(username);
        gameSessionPlayer.setUser(user);
        gameSessionPlayer.setGameSession(gameSession);
        gameSessionPlayer.setIsTurn(false);
        gameSessionPlayerRepository.save(gameSessionPlayer);

        gameSession.getPlayers().add(gameSessionPlayer);
        gameSessionRepository.save(gameSession);

        return new GameSessionResponseDto(gameSession.getId(), gameSession.getStatus(), gameSession.getMaxPlayers(), gameSession.getCreatedAt(), gameSession.getGameCode());
    }

    public GameStateDto retrieveGameState(final String username, final String gameCode) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        int currentHand = gameSession.getCurrentRound() == null ? 0 : gameSession.getCurrentRound();

        if (currentHand == 0) {
            currentHand = 1;
            gameSession.setCurrentRound(1);
            gameSessionRepository.save(gameSession);
        }

        List<Integer> roundMap = getRoundDetails(currentHand);

        Round round = roundRepository.findByGameSessionIdAndRoundNumber(gameSession.getId(), roundMap.get(2));

        if (roundMap.get(1) == 1 && round == null) {
            RoundStart roundStart = CardService.dealHand(roundMap.getFirst(), gameSession.getPlayers().size());
            round = new Round();
            round.setGameSession(gameSession);
            round.setRoundNumber(roundMap.get(2));
            round.setTrumpCard(roundStart.trumpCard());
            round.setIsComplete(false);
            roundRepository.save(round);

            for (int i = 0; i < gameSession.getMaxPlayers(); i++) {
                gameSession.getPlayers().get(i).setCards(roundStart.hands().get(i));
                gameSessionPlayerRepository.save(gameSession.getPlayers().get(i));
            }

            if (roundMap.get(2) != 1) {
                GameSessionPlayer gameSessionPlayer = gameSession.getPlayers().stream().filter(GameSessionPlayer::getIsTurn).findFirst().get();
                int previousPlayerIndex = gameSession.getPlayers().stream()
                        .sorted((p1, p2) -> (int) (p1.getId() - p2.getId()))
                        .toList()
                        .indexOf(gameSessionPlayer);
                int currentPlayerIndex = (previousPlayerIndex + 1) % gameSession.getPlayers().size();
                gameSession.getPlayers().get(previousPlayerIndex).setIsTurn(false);
                gameSession.getPlayers().get(currentPlayerIndex).setIsTurn(true);
                gameSession.setMoveOrder(currentPlayerIndex);
            }

            Round previousRound = roundRepository.findByGameSessionIdAndRoundNumber(gameSession.getId(), roundMap.get(2) - 1);
            if (previousRound != null) {
                previousRound.setIsComplete(true);
                roundRepository.save(previousRound);
            }
        }

        Optional<GameSessionPlayer> gameSessionPlayerOptional = gameSessionPlayerRepository
                .findGameSessionPlayerByGameSessionIdAndUsername(gameSession.getId(), username);
        GameSessionPlayer gameSessionPlayer = gameSessionPlayerOptional.orElseThrow();

        List<RoundMove> currentTrickMoves = roundService.getLastIncompleteTrick(round.getMoves());
        List<CardDto> cardsPlayed = CardDto.fromRoundMoveList(currentTrickMoves);
        CardDto trumpCard = CardDto.fromEntity(round.getTrumpCard());
        CardDto leadingCard = cardsPlayed.isEmpty() ? null : cardsPlayed.getFirst();
        boolean isPlayerTurn = gameSession.getPlayers().stream()
                .sorted((p1, p2) -> (int) (p1.getId() - p2.getId()))
                .toList()
                .indexOf(gameSessionPlayer) == gameSession.getMoveOrder();
        List<String> playerUsernameOrder = new ArrayList<>();
        List<GameSessionPlayer> sortedPlayers = gameSession.getPlayers().stream()
                .sorted((p1, p2) -> (int) (p1.getId() - p2.getId()))
                .toList();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            playerUsernameOrder.add(sortedPlayers.get((gameSession.getMoveOrder() + i) % sortedPlayers.size()).getUser().getUsername());
        }

        return new GameStateDto(
                new UserCardHandDto(username, CardDto.fromEntityListWithValidation(gameSessionPlayer.getCards(), leadingCard, trumpCard), isPlayerTurn, playerUsernameOrder),
                cardsPlayed,
                trumpCard,
                computeScoreboard(gameSession)
        );

    }

    private ScoreboardDto computeScoreboard(final GameSession gameSession) {
        List<UserScoreDto> userScores = new ArrayList<>();
        List<Round> rounds = roundRepository.findByGameSessionId(gameSession.getId());
        gameSession.getPlayers().forEach(
                player -> {
                    List<ScoreDetailsDto> scoreDetails = new ArrayList<>();
                    for (Round round : rounds) {
                        Bid bid = bidRepository.findByRoundIdAndGameSessionPlayerId(round.getId(), player.getId());
                        scoreDetails.add(new ScoreDetailsDto(round.getRoundNumber(), bid != null ? bid.getBidValue() : null, bid != null ? bid.getTricksWon() : null));
                    }
                    userScores.add(new UserScoreDto(player.getUser().getUsername(), scoreDetails));
                }
        );
        return new ScoreboardDto(userScores);
    }

    public List<String> getJoinedPlayers(final String gameCode) {
        GameSession gameSession = gameSessionRepository.findByGameCode(gameCode).orElseThrow();
        return gameSession.getPlayers().stream()
                .sorted((p1, p2) -> (int) (p1.getId() - p2.getId()))
                .map(player -> player.getUser().getUsername())
                .toList();
    }
}
