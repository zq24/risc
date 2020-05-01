package ece651.risc.team11;

import java.util.Random;

/**
 * This class represents a dice that will be used to resolve combat result.
 */
public class Dice {
    private int size;
    private Random random;

    public Dice(int size) {
        this.size = size;
        random = new Random();
    }

    public int rollDice() {
        return random.nextInt(size);
    }
}