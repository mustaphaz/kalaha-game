package com.kalaha.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {
    @Mock
    Board mockBoard;
    private Game subject;

    @BeforeEach
    void init() {
        subject = new Game(mockBoard);
    }

    @Test
    @DisplayName("play should call board makeMove")
    void play() {
        int index = 0;

        subject.play(index);

        verify(mockBoard).makeMove(eq(index));
    }

    @Test
    @DisplayName("getOffsetNorthPlayer should return correct offset")
    void getOffsetNorthPlayer() {
        List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        int correctOffset = 7;
        when(mockBoard.getPitList()).thenReturn(givenList);

        int result = subject.getOffsetNorthPlayer();

        assertThat(result).isEqualTo(correctOffset);
    }

    @Test
    @DisplayName("getPitListSouth should return correct pitList")
    void getPitListSouth() {
        List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        List<Integer> expectedList = Arrays.asList(0, 0, 8, 8, 8, 8);
        when(mockBoard.getPitList()).thenReturn(givenList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);

        List<Integer> result = subject.getPitListSouth();

        assertThat(result).containsExactlyElementsOf(expectedList);
    }

    @Test
    @DisplayName("getPitListNorth should return correct pitList")
    void getPitListNorth() {
        List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        List<Integer> expectedList = Arrays.asList(7, 7, 6, 6, 6, 6);
        when(mockBoard.getPitList()).thenReturn(givenList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);
        when(mockBoard.getIndexKalahaNorth()).thenReturn(13);

        List<Integer> result = subject.getPitListNorth();

        assertThat(result).containsExactlyElementsOf(expectedList);
    }

    @Test
    @DisplayName("getStonesKalahaSouth should return correct amount of stones")
    void getStonesKalahaSouth() {
        int expectedStones = 2;
        List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        when(mockBoard.getPitList()).thenReturn(givenList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);

        int result = subject.getStonesKalahaSouth();

        assertThat(result).isEqualTo(expectedStones);
    }

    @Test
    @DisplayName("isSouthTurn calls the isSouthTurn method of its board")
    void isSouthTurn() {
        subject.isSouthTurn();

        verify(mockBoard, times(1)).isSouthTurn();
    }

    @Test
    @DisplayName("setSouthTurn calls the setSouthTurn method of its board")
    void setSouthTurn() {
        subject.setSouthTurn(false);

        verify(mockBoard, times(1)).setSouthTurn(eq(false));
    }

    @Test
    @DisplayName("isGameOver calls the isGameOver method of its board")
    void isGameOver() {
        subject.isGameOver();

        verify(mockBoard, times(1)).isGameOver();
    }

    @Test
    @DisplayName("isPitEmpty calls the isEmpty method of its board")
    void isPitEmpty() {
        subject.isPitEmpty(5);

        verify(mockBoard, times(1)).isEmpty(eq(5));
    }

    @Test
    @DisplayName("getStonesKalahaNorth should return correct amount of stones")
    void getStonesKalahaNorth() {
        int expectedStones = 3;
        List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 3);
        when(mockBoard.getPitList()).thenReturn(givenList);
        when(mockBoard.getIndexKalahaNorth()).thenReturn(13);

        int result = subject.getStonesKalahaNorth();

        assertThat(result).isEqualTo(expectedStones);
    }

    @Nested
    class GetWinnerMessage {

        @BeforeEach
        void initGetWinnerMessage() {
            when(mockBoard.getIndexKalahaSouth()).thenReturn(6);
            when(mockBoard.getIndexKalahaNorth()).thenReturn(13);
        }

        @Test
        @DisplayName("should return correct message when playerSouth has won")
        void shouldReturnCorrectmessageWhenPlayerSouthHasWon() {
            String expectedMessage = "Player South has won!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 47, 0, 0, 0, 0, 0, 0, 25);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when playerNorth has won")
        void shouldReturnCorrectmessageWhenPlayerNorthHasWon() {
            String expectedMessage = "Player North has won!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 47);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when neither player has won")
        void shouldReturnCorrectmessageWhenNeitherPlayerHasWon() {
            String expectedMessage = "It's a tie!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }
    }
}