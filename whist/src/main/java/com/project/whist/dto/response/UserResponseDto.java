package com.project.whist.dto.response;

import com.project.whist.model.User;

import java.time.Instant;

public record UserResponseDto(Long id, String username, String email, Integer wins, Integer losses, Integer draws, Integer totalGames, Instant createdAt) {

    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getWins(), user.getLosses(), user.getDraws(), user.getTotalGames(), user.getCreatedAt());
    }
}
