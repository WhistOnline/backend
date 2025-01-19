package com.project.whist.dto.response;

import com.project.whist.dto.request.CardDto;
import com.project.whist.dto.request.UserCardHandDto;

import java.util.List;
import java.util.Map;

public record GameStateDto(UserCardHandDto hand, List<CardDto> cardsPlayed, CardDto trumpCard, ScoreboardDto scoreBoard) {
}
