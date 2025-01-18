package com.project.whist.dto;

import com.project.whist.model.Card;

import java.util.List;

public record RoundStart(List<List<Card>> hands, Card trumpCard) {
}
