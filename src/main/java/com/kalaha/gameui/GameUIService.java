package com.kalaha.gameui;

import com.kalaha.Game;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GameUIService {

    private static List<Integer> getListInReverse(final List<Integer> list) {
        Collections.reverse(list);
        return list;
    }

    BoardHtmlData getBoardHtmlDataFrom(final Game game) {
        return BoardHtmlData.builder()
                .rowSouth(game.getPitListSouth())
                .kalahaSouth(game.getStonesKalahaSouth())
                .rowNorth(getListInReverse(game.getPitListNorth()))
                .kalahaNorth(game.getStonesKalahaNorth())
                .build();
    }
}
