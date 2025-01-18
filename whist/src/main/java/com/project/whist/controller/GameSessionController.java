package com.project.whist.controller;

import com.project.whist.dto.request.RoundPlayDto;
import com.project.whist.dto.request.JoinGameSessionDto;
import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.GameSessionResponseDto;
import com.project.whist.dto.response.GameStateDto;
import com.project.whist.dto.response.RoundResultDto;
import com.project.whist.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public GameStateDto retrieveGame(@RequestParam String username, @RequestParam String gameCode) {
        return gameSessionService.retrieveGame(username, gameCode);
    }

//    @GetMapping("/start")
//    public List<RoundResultDto> startGameSession(@RequestParam Long gameSessionId, @RequestBody RoundPlayDto roundPlayDto) {
//        return gameSessionService.playRound(gameSessionId, roundPlayDto);
//    }
}
