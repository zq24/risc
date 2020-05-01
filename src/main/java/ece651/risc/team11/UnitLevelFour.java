package ece651.risc.team11;

public class UnitLevelFour extends Unit {

    private static final int BONUS_NUMBER = 8;

    private static final int TYPE = 4;

    public UnitLevelFour(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}