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
    private final boolean southTurn;

    public GameConfig(@Value("${board.pitsPerPlayer: 6}") final int pitsPerPlayer,
                      @Value("${board.stonesPerPit: 6}") final int stonesPerPit,
                      @Value("${board.southTurn: true}") final boolean southTurn) {
        this.pitsPerPlayer = pitsPerPlayer;
        this.stonesPerPit = stonesPerPit;
        this.southTurn = southTurn;
    }

    @Bean
    public Game getGameBean() {
        return Game.builder().board(getBoardBean()).build();
    }

    @Bean
    public Board getBoardBean() {
        return BoardImpl.builder()
                .pitList(pitsPerPlayer, stonesPerPit)
                .southTurn(southTurn)
                .build();
    }
}
