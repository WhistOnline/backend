package com.project.whist.dto.request;

import java.util.List;

public record UserCardHandDto(String username, Integer order, CardDto card) {
}
