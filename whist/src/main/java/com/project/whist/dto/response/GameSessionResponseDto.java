package com.project.whist.dto.response;

import java.time.Instant;

public record GameSessionResponseDto(Long id, String status, Integer maxPlayers, Instant createdAt, String gameCode) {

}
