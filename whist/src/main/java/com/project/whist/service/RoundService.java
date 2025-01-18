package com.project.whist.service;

import com.project.whist.model.RoundMove;
import com.project.whist.repository.GameSessionPlayerRepository;
import com.project.whist.repository.GameSessionRepository;
import com.project.whist.repository.RoundRepository;
import com.project.whist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final RoundRepository roundRepository;
    private final GameSessionPlayerRepository gameSessionPlayerRepository;

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
