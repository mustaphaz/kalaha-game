package com.kalaha;

import com.kalaha.domain.Board;
import com.kalaha.domain.BoardImpl;
import com.kalaha.domain.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class GameConfig extends WebMvcConfigurationSupport {
    private final int pitsPerPlayer;
    private final int stonesPerPit;
    private final String firstTurn;

    public GameConfig(@Value("${board.pitsPerPlayer}") final int pitsPerPlayer,
                      @Value("${board.stonesPerPit}") final int stonesPerPit,
                      @Value("${board.firstTurn}") final String firstTurn) {
        this.pitsPerPlayer = pitsPerPlayer;
        this.stonesPerPit = stonesPerPit;
        this.firstTurn = firstTurn;
    }

    @Bean
    public Game getGameBean() {
        return new Game(getBoardBean());
    }

    @Bean
    public Board getBoardBean() {
        return new BoardImpl(pitsPerPlayer, stonesPerPit, getSouthTurn(firstTurn));
    }

    private boolean getSouthTurn(final String firstTurn) {
        return !firstTurn.equalsIgnoreCase("north");
    }
}
