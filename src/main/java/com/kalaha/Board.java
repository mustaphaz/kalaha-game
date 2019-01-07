package com.kalaha;

import java.util.List;

public interface Board {

    void makeMove(int index);

    List<Integer> getPitList();

    int getIndexKalahaSouth();

    int getIndexKalahaNorth();

    boolean isSouthTurn();

    void setSouthTurn(boolean southTurn);

    boolean isEmpty(int index);

    boolean isGameOver();
}
