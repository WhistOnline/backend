package com.project.whist.dto.response;

public record UserScoreDto(String username, Integer roundNo, Integer bid, Integer tricksWon, Integer score) {
}
