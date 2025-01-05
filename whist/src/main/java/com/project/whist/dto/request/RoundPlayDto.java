package com.project.whist.dto.request;

import java.util.List;

public record RoundPlayDto(List<UserCardHandDto> hands, String trumpSuit, Integer roundNo) {
}
