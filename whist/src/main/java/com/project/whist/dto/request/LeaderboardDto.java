package com.project.whist.dto.request;

public record LeaderboardDto(String username, Integer gamesPlayed, Integer gamesWon, Integer totalPoints) {
}
