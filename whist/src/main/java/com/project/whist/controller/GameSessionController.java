package com.project.whist.controller;

import com.project.whist.dto.request.JoinGameSessionDto;
import com.project.whist.model.GameSession;
import com.project.whist.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-session")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping("/create")
    public GameSession createGameSession(@RequestBody String username) {
        return gameSessionService.createGameSession(username);
    }

    @PostMapping("/join")
    public GameSession joinGameSession(@RequestBody JoinGameSessionDto joinGameSessionDto) {
        return gameSessionService.joinGameSession(joinGameSessionDto);
    }
}
