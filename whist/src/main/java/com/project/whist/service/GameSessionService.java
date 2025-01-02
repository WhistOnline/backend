package com.project.whist.service;

import com.project.whist.dto.request.JoinGameSessionDto;
import com.project.whist.model.GameSession;
import com.project.whist.model.GameSessionPlayer;
import com.project.whist.model.User;
import com.project.whist.repository.GameSessionRepository;
import com.project.whist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;

    public GameSession createGameSession(final String username) {
        GameSession gameSession = new GameSession();
        GameSessionPlayer gameSessionPlayer = new GameSessionPlayer();
        User user = userRepository.findUserByUsername(username);
        gameSessionPlayer.setUser(user);
        gameSessionPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(gameSessionPlayer);
        return gameSessionRepository.save(gameSession);
    }

    public GameSession joinGameSession(final JoinGameSessionDto joinGameSessionDto) {
        GameSession gameSession = gameSessionRepository.findById(joinGameSessionDto.gameSessionId()).orElseThrow();
        GameSessionPlayer gameSessionPlayer = new GameSessionPlayer();
        User user = userRepository.findUserByUsername(joinGameSessionDto.username());
        gameSessionPlayer.setUser(user);
        gameSessionPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(gameSessionPlayer);
        return gameSessionRepository.save(gameSession);
    }
}
