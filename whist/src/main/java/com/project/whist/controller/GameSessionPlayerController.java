package com.project.whist.controller;

import com.project.whist.dto.request.LeaderboardDto;
import com.project.whist.service.GameSessionPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class GameSessionPlayerController {
    private final GameSessionPlayerService gameSessionPlayerService;
    @GetMapping("/leaderboard")
    public List<LeaderboardDto> getLeaderboard() {
        return gameSessionPlayerService.getLeaderboard();
    }
}
