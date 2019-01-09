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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {
    private Game game;

    @BeforeEach
    void init() {
        Board board = BoardImpl.builder()
                .pitList(6, 6)
                .southTurn(true)
                .build();
        game = new Game(board);
    }

    @Test
    @DisplayName("play updates the board")
    void play() {
        int index = 0;
        List<Integer> expectedPitList = Arrays.asList(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0);

        game.play(index);

        assertThat(game.getBoard().getPitList()).isEqualTo(expectedPitList);
    }

    @Test
    @DisplayName("getOffsetPlayerNorth should return correct offset")
    void getOffsetNorthPlayer() {
        int correctOffset = 7;

        int result = game.getOffsetPlayerNorth();

        assertThat(result).isEqualTo(correctOffset);
    }

    @Test
    @DisplayName("getPitListSouth should return correct pitList")
    void getPitListSouth() {
        List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 6, 6);

        List<Integer> result = game.getPitListSouth();

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    @DisplayName("getPitListNorth should return correct pitList")
    void getPitListNorth() {
        List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 6, 6);

        List<Integer> result = game.getPitListNorth();

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    @DisplayName("changes in pitListSouth are not reflected in original list")
    void changesInPitListSouthNotReflected() {
        List<Integer> expectedPitList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> pitListSouth = game.getPitListSouth();

        pitListSouth.add(56);
        pitListSouth.add(66);

        List<Integer> actualPitListAfterChange = game.getBoard().getPitList();
        assertThat(actualPitListAfterChange).isEqualTo(expectedPitList);
    }

    @Test
    @DisplayName("changes in pitListNorth are not reflected in original list")
    void changesInPitListNorthNotReflected() {
        List<Integer> expectedPitList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> pitListNorth = game.getPitListNorth();

        pitListNorth.clear();

        List<Integer> actualPitListAfterChange = game.getBoard().getPitList();
        assertThat(actualPitListAfterChange).isEqualTo(expectedPitList);
    }

    @Test
    @DisplayName("isSouthTurn returns true for a new board")
    void isSouthTurn() {
        assertThat(game.isSouthTurn()).isTrue();
    }

    @Test
    @DisplayName("setSouthTurn returns false if set to false")
    void setSouthTurn() {
        game.setSouthTurn(false);

        assertThat(game.isSouthTurn()).isFalse();
    }

    @Test
    @DisplayName("isGameOver returns false for new board")
    void isGameOver() {
        boolean result = game.isGameOver();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isPitEmpty returns true for empty pit")
    void isPitEmpty() {
        boolean result = game.isPitEmpty(6);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("getStonesKalahaNorth should return correct amount of stones")
    void getStonesKalahaNorth() {
        int expectedStones = 0;

        int result = game.getStonesKalahaNorth();

        assertThat(result).isEqualTo(expectedStones);
    }

    @Test
    @DisplayName("getStonesKalahaSouth should return correct amount of stones")
    void getStonesKalahaSouth() {
        int expectedStones = 0;

        int result = game.getStonesKalahaSouth();

        assertThat(result).isEqualTo(expectedStones);
    }

    @Nested
    class GetWinnerMessage {
        @Mock
        Board mockBoard;
        private Game gameWithMockBoard;

        @BeforeEach
        void initGetWinnerMessage() {
            gameWithMockBoard = new Game(mockBoard);
            when(mockBoard.getIndexKalahaSouth()).thenReturn(6);
            when(mockBoard.getIndexKalahaNorth()).thenReturn(13);
        }

        @Test
        @DisplayName("should return correct message when player south has won")
        void shouldReturnCorrectmessageWhenPlayerSouthHasWon() {
            String expectedMessage = "Player South has won!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 47, 0, 0, 0, 0, 0, 0, 25);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = gameWithMockBoard.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when player north has won")
        void shouldReturnCorrectmessageWhenPlayerNorthHasWon() {
            String expectedMessage = "Player North has won!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 47);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = gameWithMockBoard.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("should return correct message when neither player has won")
        void shouldReturnCorrectmessageWhenNeitherPlayerHasWon() {
            String expectedMessage = "It's a tie!";
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36);
            when(mockBoard.getPitList()).thenReturn(givenList);

            String result = gameWithMockBoard.getWinnerMessage();

            assertThat(result).isEqualTo(expectedMessage);
        }
    }
}