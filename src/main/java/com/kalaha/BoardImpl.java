package com.kalaha;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardImpl implements Board {

    private static final int STONES_PER_PIT = 6;
    private static final int AMOUNT_OF_PITS = 6;
    private static final int TOTAL_AMOUNT_OF_PITS = 2 * AMOUNT_OF_PITS + 2;
    private boolean southTurn;
    private List<Integer> pitList;

    BoardImpl() {
        List<Integer> list = IntStream.
                generate(() -> STONES_PER_PIT)
                .limit(TOTAL_AMOUNT_OF_PITS)
                .boxed()
                .collect(Collectors.toList());
        list.set(getIndexKalahaSouth(), 0);
        list.set(getIndexKalahaNorth(), 0);
        this.pitList = list;
        this.southTurn = true;
    }

    @Override
    public void makeMove(final int index) {
        int lastPit = allocateAndGetLastPit(index);
        captureIfLastPitIsOwnEmptyPit(lastPit);
        collectLastStonesIfGameIsOver();
        switchTurnsIfLastPitIsNotOwnKalaha(lastPit);
    }

    @Override
    public List<Integer> getPitList() {
        return pitList;
    }

    @Override
    public int getIndexKalahaSouth() {
        return TOTAL_AMOUNT_OF_PITS / 2 - 1;
    }

    @Override
    public int getIndexKalahaNorth() {
        return TOTAL_AMOUNT_OF_PITS - 1;
    }

    @Override
    public boolean isSouthTurn() {
        return southTurn;
    }

    @Override
    public void setSouthTurn(final boolean isSouthTurn) {
        southTurn = isSouthTurn;
    }

    @Override
    public boolean isEmpty(final int index) {
        return getStonesInPit(index) == 0;
    }

    @Override
    public boolean isGameOver() {
        return getTotalStonesInPitsSouth() == 0 || getTotalStonesInPitsNorth() == 0;
    }

    private int allocateAndGetLastPit(final int index) {
        int stones = getStonesInPit(index);
        int lastPit = index;
        while (stones > 0) {
            lastPit = nextPit(lastPit);
            incrementStonesInPit(lastPit);
            --stones;
        }
        setStonesInPit(index, 0);
        return lastPit;
    }

    private int nextPit(final int index) {
        int nextIndex = index == getIndexKalahaNorth() ? 0 : index + 1;
        return skipOtherPlayersKalaha(nextIndex);
    }

    private void incrementStonesInPit(final int index) {
        setStonesInPit(index, getStonesInPit(index) + 1);
    }

    private void captureIfLastPitIsOwnEmptyPit(final int index) {
        boolean pitContainsOneStone = getStonesInPit(index) == 1;
        boolean landsInPlayersOwnPit = isSouthTurn() && index < getIndexKalahaSouth()
                || isNotSouthTurn() && index > getIndexKalahaSouth();

        if (pitContainsOneStone && isARegularPit(index) && landsInPlayersOwnPit) {
            int kalaha = isSouthTurn() ? getIndexKalahaSouth() : getIndexKalahaNorth();
            int capturedStones = getStonesInPit(index) + getStonesInPit(oppositePit(index));

            setStonesInPit(kalaha, getStonesInPit(kalaha) + capturedStones);
            setStonesInPit(index, 0);
            setStonesInPit(oppositePit(index), 0);
        }
    }

    private boolean isARegularPit(final int index) {
        return index != getIndexKalahaSouth() && index != getIndexKalahaNorth();
    }

    private int oppositePit(final int index) {
        return 2 * getIndexKalahaSouth() - index;
    }

    private void collectLastStonesIfGameIsOver() {
        if (isGameOver()) {
            int lastStonesSouth = getTotalStonesInPitsSouth();
            int lastStonesNorth = getTotalStonesInPitsNorth();

            collectStones(lastStonesSouth, lastStonesNorth);
            emptyRegularPits();
        }
    }

    private void collectStones(final int lastStonesSouth, final int lastStonesNorth) {
        int newAmountSouth = getStonesInPit(getIndexKalahaSouth()) + lastStonesSouth;
        int newAmountNorth = getStonesInPit(getIndexKalahaNorth()) + lastStonesNorth;

        setStonesInPit(getIndexKalahaSouth(), newAmountSouth);
        setStonesInPit(getIndexKalahaNorth(), newAmountNorth);

    }

    private void emptyRegularPits() {
        IntStream.range(0, getIndexKalahaSouth())
                .forEach(index -> setStonesInPit(index, 0));

        IntStream.range(getIndexKalahaSouth() + 1, getIndexKalahaNorth())
                .forEach(index -> setStonesInPit(index, 0));
    }

    private void switchTurnsIfLastPitIsNotOwnKalaha(final int lastPit) {
        if (isNotOwnKalaha(lastPit)) {
            setSouthTurn(isNotSouthTurn());
        }
    }

    private boolean isNotOwnKalaha(final int index) {
        return isSouthTurn() && index != getIndexKalahaSouth()
                || isNotSouthTurn() && index != getIndexKalahaNorth();
    }

    private int getStonesInPit(final int index) {
        return getPitList().get(index);
    }

    private void setStonesInPit(final int index, int value) {
        getPitList().set(index, value);
    }

    private int getTotalStonesInPitsSouth() {
        return IntStream.range(0, getIndexKalahaSouth())
                .map(this::getStonesInPit)
                .sum();
    }

    private int getTotalStonesInPitsNorth() {
        return IntStream.range(getIndexKalahaSouth() + 1, getIndexKalahaNorth())
                .map(this::getStonesInPit)
                .sum();
    }

    private int skipOtherPlayersKalaha(final int index) {
        if (isSouthTurn() && index == getIndexKalahaNorth())
            return 0;
        if (isNotSouthTurn() && index == getIndexKalahaSouth())
            return index + 1;
        return index;
    }

    private boolean isNotSouthTurn() {
        return !isSouthTurn();
    }

}
