package com.project.whist.service.initializer;

import com.project.whist.service.CardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DeckInitializer implements CommandLineRunner {
    private final CardService cardService;

    public DeckInitializer(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public void run(String... args) {
        cardService.initializeDeck();
    }
}