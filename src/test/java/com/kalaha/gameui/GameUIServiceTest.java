package com.kalaha.gameui;

import com.kalaha.domain.Board;
import com.kalaha.domain.BoardImpl;
import com.kalaha.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class GameUIServiceTest {

    private GameUIService subject;
    private Game game;

    @BeforeEach
    void init() {
        Board board = BoardImpl.builder()
                .pitList(6, 6)
                .southTurn(true)
                .build();
        game = Game.builder().board(board).build();
        subject = new GameUIService();
    }

    @Nested
    class GetBoardHtmlDate {
        @Test
        @DisplayName("from start returns correct data")
        void getBoardHtmlDataFromStartBoardReturnsCorrectBoardHtmlData() {
            BoardHtmlData expected = BoardHtmlData.builder()
                    .rowSouth(Arrays.asList(6, 6, 6, 6, 6, 6))
                    .kalahaSouth(0)
                    .rowNorth(Arrays.asList(6, 6, 6, 6, 6, 6))
                    .kalahaNorth(0)
                    .build();

            BoardHtmlData result = subject.getBoardHtmlDataFrom(game);

            verifyBoardHtmlData(result, expected);
        }

        @Test
        @DisplayName("after play returns correct data")
        void getBoardHtmlDataAfterPlayingBoardReturnsCorrectBoardHtmlData() {
            BoardHtmlData expected = BoardHtmlData.builder()
                    .rowSouth(Arrays.asList(0, 0, 8, 8, 8, 8))
                    .kalahaSouth(2)
                    .rowNorth(Arrays.asList(6, 6, 6, 6, 7, 7))
                    .kalahaNorth(0)
                    .build();

            game.play(0);
            game.play(1);

            BoardHtmlData result = subject.getBoardHtmlDataFrom(game);

            verifyBoardHtmlData(result, expected);
        }

        private void verifyBoardHtmlData(BoardHtmlData result, BoardHtmlData expected) {
            assertThat(result.rowSouth).containsSequence(expected.rowSouth);
            assertThat(result.kalahaSouth).isEqualTo(expected.kalahaSouth);
            assertThat(result.rowNorth).containsSequence(expected.rowNorth);
            assertThat(result.kalahaNorth).isEqualTo(expected.kalahaNorth);
        }
    }
}
