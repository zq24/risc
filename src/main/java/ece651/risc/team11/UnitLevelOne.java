package ece651.risc.team11;

public class UnitLevelOne extends Unit {

    private static final int BONUS_NUMBER = 1;

    private static final int TYPE = 1;

    public UnitLevelOne(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}