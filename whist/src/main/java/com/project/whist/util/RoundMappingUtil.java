package com.project.whist.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundMappingUtil {

    public static final Map<Integer, List<Integer>> ROUND_TO_CARD_MAP = new HashMap<>();

    static {
        int[] handsPerRound = {
                1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1
        };

        int round = 1;
        int cards = 1;
        int actualRound = 1;
        boolean isFirst;

        for (int handsInRound : handsPerRound) {
            isFirst = true;
            for (int i = 0; i < handsInRound; i++) {
                ROUND_TO_CARD_MAP.put(round++, List.of(cards, isFirst ? 1 : 0, actualRound));
                isFirst = false;
            }
            actualRound++;
            cards = handsInRound;
        }
    }

    public static List<Integer> getRoundDetails(int round) {
        return ROUND_TO_CARD_MAP.getOrDefault(round, List.of(-1, -1, -1));
    }
}
