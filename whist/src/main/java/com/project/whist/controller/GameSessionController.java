package com.project.whist.controller;

import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.GameSessionResponseDto;
import com.project.whist.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-session")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping("/create")
    public GameSessionResponseDto createGameSession(@RequestParam String username) {
        return gameSessionService.createGameSession(username);
    }

    @PostMapping("/join")
    public GameSessionResponseDto joinGameSession(@RequestParam String username, @RequestParam String gameCode) {
        return gameSessionService.joinGameSession(username, gameCode);
    }

    @GetMapping
    public GameStateDto retrieveGameState(@RequestParam String username, @RequestParam String gameCode) {
        return gameSessionService.retrieveGameState(username, gameCode);
    }
}
