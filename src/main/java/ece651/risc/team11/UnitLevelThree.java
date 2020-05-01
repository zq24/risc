package ece651.risc.team11;

public class UnitLevelThree extends Unit {

    private static final int BONUS_NUMBER = 5;

    private static final int TYPE = 3;

    public UnitLevelThree(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}