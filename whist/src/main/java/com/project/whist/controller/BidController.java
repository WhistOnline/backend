package com.project.whist.controller;

import com.project.whist.dto.request.BidDto;
import com.project.whist.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {
    private final BidService bidService;

    @PostMapping
    public BidDto bid(@RequestParam String username, @RequestParam String gameCode, @RequestParam Integer bidValue) {
        return bidService.bid(username, gameCode, bidValue);
    }
}
