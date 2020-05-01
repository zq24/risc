package ece651.risc.team11;

/**
 * This class represents the concrete implementation of Unit.
 */
public class NormalUnit extends Unit {
    private static final long serialVersionUID = 651L;

    private static final int BONUS_NUMBER = 0;

    private static final int TYPE = 0;

    public NormalUnit(int ownerId) {
        this.ownerId = ownerId;
        this.bonus = BONUS_NUMBER;
        this.type = TYPE;
    }
}