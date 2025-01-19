package com.project.whist.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.project.whist.util.RoundMappingUtil.getRoundDetails;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundMappingTest {

    @Test
    public void testRoundMapping() {

        List<Integer> expected = List.of(3, 0, 7);
        List<Integer> actual = getRoundDetails(11);
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundDetailsFirstRound() {
        List<Integer> expected = List.of(1, 1, 1);
        List<Integer> actual = getRoundDetails(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundDetailsLastRound() {
        List<Integer> expected = List.of(-1, -1, -1);
        List<Integer> actual = getRoundDetails(96);
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundDetailsInvalidRound() {
        List<Integer> expected = List.of(-1, -1, -1);
        List<Integer> actual = getRoundDetails(100);
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundDetailsMiddleRound() {
        List<Integer> expected = List.of(7, 0, 14);
        List<Integer> actual = getRoundDetails(57);
        assertEquals(expected, actual);
    }
}
