package com.kalaha;

import java.util.List;

public interface Board {

    void makeMove(int index);

    List<Integer> getPitList();

    int getIndexKalahaSouth();

    int getIndexKalahaNorth();

    void setSouthTurn(boolean southTurn);

    boolean isSouthTurn();

    boolean isEmpty(int index);

    boolean isGameOver();
}
