package com.project.whist.service;

import com.project.whist.model.RoundMove;
import com.project.whist.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final RoundRepository roundRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;
    private final BidRepository bidRepository;

    public List<RoundMove> getLastIncompleteTrick(List<RoundMove> moves) {
        if (moves == null || moves.isEmpty()) {
            return List.of();
        }

        int size = moves.size();
        int remainder = size % 4;

        if (remainder == 0) {
            return List.of();
        }

        return moves.stream()
                .skip(size - remainder)
                .collect(Collectors.toList());
    }
}
