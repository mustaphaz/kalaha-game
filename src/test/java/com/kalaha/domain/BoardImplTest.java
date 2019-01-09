package com.kalaha.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class BoardImplTest {
    private Board subject;

    @BeforeEach
    void init() {
        subject = BoardImpl.builder()
                .pitList(6, 6)
                .southTurn(true)
                .build();
    }

    @Test
    @DisplayName("A new board should have a correct pitList and southTurn set to true")
    void getPitListNewBoard() {
        List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);

        List<Integer> result = subject.getPitList();

        assertThat(result).containsExactlyElementsOf(expectedList);
        assertThat(subject.isSouthTurn()).isTrue();
    }

    @Nested
    class MakeMove {
        @Test
        @DisplayName("returns correct pitList after single move of player south")
        void singleMoveSouth() {
            List<Integer> givenList = Arrays.asList(0, 1, 8, 8, 8, 8, 2, 6, 0, 7, 7, 8, 8, 1);
            List<Integer> expectedList = Arrays.asList(0, 0, 9, 8, 8, 8, 2, 6, 0, 7, 7, 8, 8, 1);
            addPitListToBoard(subject, givenList);

            subject.makeMove(1);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("returns correct pitList after single move of player player north")
        void singleMoveNorth() {
            List<Integer> expectedList = Arrays.asList(7, 7, 7, 7, 7, 7, 0, 0, 7, 7, 7, 7, 0, 2);

            subject.setSouthTurn(false);
            subject.makeMove(7);
            subject.makeMove(12);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("returns correct pitList after multiple moves")
        void multipleMoves() {
            List<Integer> expectedList = Arrays.asList(2, 1, 9, 9, 8, 8, 2, 0, 0, 8, 8, 8, 8, 1);

            subject.makeMove(0);
            subject.makeMove(1);
            subject.makeMove(7);
            subject.makeMove(8);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("skips kalaha opponent when player south plays")
        void skipKalahaOpponentSouth() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 8, 0, 6, 6, 6, 6, 6, 6, 0);
            List<Integer> expectedList = Arrays.asList(7, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 7, 0);
            addPitListToBoard(subject, givenList);

            subject.makeMove(5);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("skips kalaha opponent when player north plays")
        void skipKalahaOpponentNorth() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 8, 0);
            List<Integer> expectedList = Arrays.asList(7, 7, 7, 7, 7, 7, 0, 7, 6, 6, 6, 6, 0, 1);
            addPitListToBoard(subject, givenList);
            subject.setSouthTurn(false);

            subject.makeMove(12);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("does not switch turn when last stone lands in own kalaha")
        void notSwitchTurn() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 1, 0, 6, 6, 6, 6, 6, 6, 0);
            List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 6, 0, 1, 6, 6, 6, 6, 6, 6, 0);
            addPitListToBoard(subject, givenList);

            assertThat(subject.isSouthTurn()).isTrue();

            subject.makeMove(5);

            assertThat(subject.isSouthTurn()).isTrue();
            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("does switch turn when last stone lands not in own kalaha")
        void switchTurn() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 2, 0, 6, 6, 6, 6, 6, 6, 0);
            List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 6, 0, 1, 7, 6, 6, 6, 6, 6, 0);
            addPitListToBoard(subject, givenList);

            assertThat(subject.isSouthTurn()).isTrue();

            subject.makeMove(5);

            assertThat(subject.isSouthTurn()).isFalse();
            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("does capture when last stone lands in own empty pit")
        void capture() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 1, 0, 0, 6, 6, 6, 6, 6, 6, 0);
            List<Integer> expectedList = Arrays.asList(6, 6, 6, 6, 0, 0, 7, 0, 6, 6, 6, 6, 6, 0);
            addPitListToBoard(subject, givenList);

            subject.makeMove(4);

            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }

        @Test
        @DisplayName("does collect last stones when game is over")
        void gameOverCollect() {
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 1, 5, 5, 6, 6, 6, 6, 6, 10);
            List<Integer> expectedList = Arrays.asList(0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 45);
            addPitListToBoard(subject, givenList);

            subject.makeMove(5);

            assertThat(subject.isGameOver()).isTrue();
            assertThat(subject.getPitList()).containsExactlyElementsOf(expectedList);
        }
    }

    @Test
    @DisplayName("getIndexKalahaSouth returns correct index")
    void getIndexKalahaSouth() {
        int expectedIndex = 6;

        int result = subject.getIndexKalahaSouth();

        assertThat(result).isEqualTo(expectedIndex);
    }

    @Test
    @DisplayName("getIndexKalahaNorth returns correct index")
    void getIndexKalahaNorth() {
        int expectedIndex = 13;

        int result = subject.getIndexKalahaNorth();

        assertThat(result).isEqualTo(expectedIndex);
    }

    @Test
    @DisplayName("isEmpty returns true if pit is empty")
    void isEmpty() {
        subject.makeMove(4);

        assertThat(subject.isEmpty(4)).isTrue();
    }

    @Test
    @DisplayName("isEmpty returns false if pit is not empty")
    void isNotEmpty() {
        List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 1, 0, 0, 6, 6, 6, 6, 6, 6, 0);
        addPitListToBoard(subject, givenList);

        subject.makeMove(0);

        assertThat(subject.isEmpty(1)).isFalse();
    }

    @Nested
    class GameOver {
        @Test
        @DisplayName("returns true if south player has no stones left")
        void noStonesSouthPlayer() {
            List<Integer> givenList = Arrays.asList(6, 6, 6, 6, 6, 5, 5, 0, 0, 0, 0, 0, 0, 47);
            addPitListToBoard(subject, givenList);

            boolean result = subject.isGameOver();

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("returns true if north player has no stones left")
        void noStonesNorthPlayer() {
            List<Integer> givenList = Arrays.asList(0, 0, 0, 0, 0, 0, 5, 5, 6, 6, 6, 6, 6, 47);
            addPitListToBoard(subject, givenList);

            boolean result = subject.isGameOver();

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("returns false if south player has stones left")
        void stonesLeftSouthPlayer() {
            List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
            addPitListToBoard(subject, givenList);

            boolean result = subject.isGameOver();

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("returns false if north player has no stones left")
        void stonesLeftNorthPlayer() {
            List<Integer> givenList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
            addPitListToBoard(subject, givenList);

            boolean result = subject.isGameOver();

            assertThat(result).isFalse();
        }
    }

    private void addPitListToBoard(Board board, List<Integer> pitList) {
        IntStream.range(0, pitList.size())
                .forEach(index -> board.getPitList().set(index, pitList.get(index)));
    }
}
