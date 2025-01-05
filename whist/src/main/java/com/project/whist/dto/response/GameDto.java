package com.project.whist.dto.response;

import com.project.whist.model.Card;

import java.util.List;
import java.util.Map;

//public record GameDto(Long gameSessionId,
//                      Integer roundNumber,
//                      String trumpSuit,
//                      Map<String, List<Card>> hands,
//                      Map<String, List<Integer>> possibleMoves,
//                      Integer playerCount) {
//}

public record GameDto(Long gameSessionId) {
}

