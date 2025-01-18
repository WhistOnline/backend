package com.project.whist.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.project.whist.util.RoundMappingUtil.getRoundDetails;

public class RoundMappingTest {

    @Test
    public void testRoundMapping() {

        List<Integer> expected = List.of(3, 0, 7);
        List<Integer> actual = getRoundDetails(11);

        assert expected.equals(actual);
    }
}
