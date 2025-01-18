package com.project.whist.controller;

import com.project.whist.dto.request.BidDto;
import com.project.whist.dto.request.CardDto;
import com.project.whist.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/play-card")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public BidDto playCard(@RequestParam String username, @RequestParam String gameCode, @RequestParam CardDto card) {
        return cardService.playCard(username, gameCode, card);
    }
}
