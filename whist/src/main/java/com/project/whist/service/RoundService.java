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

    static final Map<Integer, Integer> numberMap = Map.ofEntries(
            Map.entry(1, 1), Map.entry(2, 1), Map.entry(3, 1), Map.entry(4, 1),
            Map.entry(5, 2), Map.entry(6, 3), Map.entry(7, 4), Map.entry(8, 5),
            Map.entry(9, 6), Map.entry(10, 7), Map.entry(11, 8), Map.entry(12, 8),
            Map.entry(13, 8), Map.entry(14, 8), Map.entry(15, 7), Map.entry(16, 6),
            Map.entry(17, 5), Map.entry(18, 4), Map.entry(19, 3), Map.entry(20, 2),
            Map.entry(21, 1), Map.entry(22, 1), Map.entry(23, 1), Map.entry(24, 1)
    );

    public static int getRoundNumber(Integer roundNumberIndex) {
        return numberMap.get(roundNumberIndex);
    }

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
