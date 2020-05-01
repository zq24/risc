package ece651.risc.team11;

public class UnitLevelSix extends Unit {

    private static final int BONUS_NUMBER = 15;

    private static final int TYPE = 6;

    public UnitLevelSix(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}