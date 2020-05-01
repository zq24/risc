package ece651.risc.team11;

public class UnitLevelFive extends Unit {

    private static final int BONUS_NUMBER = 11;

    private static final int TYPE = 5;

    public UnitLevelFive(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}