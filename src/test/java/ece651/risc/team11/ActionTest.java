package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    void getPlayerID() {
        Action action = new Attack();
        assertEquals(action.getPlayerID(), 0);
        action = new Attack(1, 2, 3, 4, 5);
        assertEquals(action.getPlayerID(), 1);
    }

    @Test
    void getStartTerritoryID() {
        Attack action = new Attack(1, 2, 3, 4, 5);
        assertEquals(action.getStartTerritoryID(), 3);
    }

    @Test
    void getEndTerritoryID() {
        Move action = new Move();
        assertEquals(action.getEndTerritoryID(), 0);
        action = new Move(1, 2, 3, 4, 5);
        assertEquals(action.getEndTerritoryID(), 4);
    }

    @Test
    void getNumOfUnits() {
        Action action = new Attack(1, 2, 3, 4, 5);
        assertEquals(action.getNumOfUnits(), 2);
    }

    @Test
    public void executeAction() {
        Action attack = new Attack();
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        try {
            attack.executeAction(new RiscMap(2, 5, colorList), new RiscPlayerInRoom());
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        attack.setNumOfUnits(1);
    }
}