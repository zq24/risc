package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void rollDice() {
        Dice dice = new Dice(20);
        for (int i = 0; i < 100; i++) {
            assertTrue(dice.rollDice() >= 0);
            assertTrue(dice.rollDice() < 20);
        }
    }
}