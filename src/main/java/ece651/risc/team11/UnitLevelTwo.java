package ece651.risc.team11;

public class UnitLevelTwo extends Unit {

    private static final int BONUS_NUMBER = 3;

    private static final int TYPE = 2;

    public UnitLevelTwo(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}