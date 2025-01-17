package com.project.whist.dto.request;

import java.util.List;

public record UserCardHandDto(String username, List<CardDto> hand, boolean isMyTurn) {
}
