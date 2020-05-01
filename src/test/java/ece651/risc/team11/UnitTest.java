package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void getName() {
        Unit unit = new NormalUnit(0);
        assertEquals(unit.getName(), "NormalUnit");
    }

    @Test
    void setName() {
        Unit unit = new NormalUnit(0);
        unit.setName("Testing");
        assertEquals(unit.getName(), "Testing");
    }

    @Test
    void getBonus() {
        Unit unit = new NormalUnit(0);
        assertEquals(unit.getBonus(), 0);
    }

    @Test
    void getType() {
        Unit unit = new NormalUnit(0);
        assertEquals(unit.getType(), 0);
    }

    @Test
    void unitClone() {
        Unit unit = new NormalUnit(0);
        try {
            unit.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}