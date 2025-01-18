package com.project.whist.dto.response;

import java.util.List;

public record UserScoreDto(String username, List<ScoreDetailsDto> scoreDetails, Integer score) {
}
