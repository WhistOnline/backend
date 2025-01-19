package com.project.whist.controller;

import com.project.whist.dto.request.CardDto;
import com.project.whist.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public CardDto playCard(@RequestParam String username, @RequestParam String gameCode, @RequestBody CardDto card) {
        return cardService.playCard(username, gameCode, card);
    }
}
