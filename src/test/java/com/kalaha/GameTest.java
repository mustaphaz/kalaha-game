package com.kalaha;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {
    @Mock
    Board mockBoard;
    private Game subject;

    @BeforeEach
    void init() {
        subject = Game.initGame();
    }

    @Test
    @DisplayName("initGame should return a game with a board a southTurn set to true and default pitlist")
    void initGame() {
        assertThat(subject.isSouthTurn()).isTrue();
        assertThat(subject.getBoard()).isNotNull();
    }

    @Test
    @DisplayName("play should call board makeMove")
    void play() {
        subject.setBoard(mockBoard);
        int index = 0;

        subject.play(index);

        verify(mockBoard).makeMove(eq(index));
    }

    @Test
    @DisplayName("getOffsetNorthPlayer should return correct offset")
    void getOffsetNorthPlayer() {
        List<Integer> pitList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        int correctOffset = 7;
        addPitListToMockBoard(pitList);

        int result = subject.getOffsetNorthPlayer();

        assertThat(result).isEqualTo(correctOffset);
    }

    @Test
    @DisplayName("getPitListSouth should return correct pitList")
    void getPitListSouth() {
        List<Integer> pitList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        List<Integer> pitListSouth = Arrays.asList(0, 0, 8, 8, 8, 8);
        addPitListToMockBoard(pitList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);

        List<Integer> result = subject.getPitListSouth();

        assertThat(result).containsExactlyElementsOf(pitListSouth);
    }

    @Test
    @DisplayName("getPitListNorth should return correct pitList")
    void getPitListNorth() {
        List<Integer> pitList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        List<Integer> pitListNorth = Arrays.asList(7, 7, 6, 6, 6, 6);
        addPitListToMockBoard(pitList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);
        when(mockBoard.getIndexKalahaNorth()).thenReturn(13);

        List<Integer> result = subject.getPitListNorth();

        assertThat(result).containsExactlyElementsOf(pitListNorth);
    }

    @Test
    @DisplayName("getStonesKalahaSouth should return correct amount of stones")
    void getStonesKalahaSouth() {
        int expectedStones = 2;
        List<Integer> pitList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        addPitListToMockBoard(pitList);
        when(mockBoard.getIndexKalahaSouth()).thenReturn(6);

        int result = subject.getStonesKalahaSouth();

        assertThat(result).isEqualTo(expectedStones);
    }

    @Test
    @DisplayName("getStonesKalahaNorth should return correct amount of stones")
    void getStonesKalahaNorth() {
        int expectedStones = 3;
        List<Integer> pitList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 3);
        addPitListToMockBoard(pitList);
        when(mockBoard.getIndexKalahaNorth()).thenReturn(13);

        int result = subject.getStonesKalahaNorth();

        assertThat(result).isEqualTo(expectedStones);
    }

    private void addPitListToMockBoard(List<Integer> pitList) {
        subject.setBoard(mockBoard);
        when(mockBoard.getPitList()).thenReturn(pitList);
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
            List<Integer> pitList = Arrays.asList(0, 0, 0, 0, 0, 0, 47, 0, 0, 0, 0, 0, 0, 25);
            addPitListToMockBoard(pitList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when playerNorth has won")
        void shouldReturnCorrectmessageWhenPlayerNorthHasWon() {
            String expectedMessage = "Player North has won!";
            List<Integer> pitList = Arrays.asList(0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 47);
            addPitListToMockBoard(pitList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when neither player has won")
        void shouldReturnCorrectmessageWhenNeitherPlayerHasWon() {
            String expectedMessage = "It's a tie!";
            List<Integer> pitList = Arrays.asList(0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36);
            addPitListToMockBoard(pitList);

            String result = subject.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }
    }
}