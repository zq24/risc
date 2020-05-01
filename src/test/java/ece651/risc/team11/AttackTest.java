package ece651.risc.team11;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackTest {

    @Test
    void validateAction() {
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
            }
        }));
        Attack attack = new Attack();
        Attack attack1 = new Attack(attack, 1, 2, 3);
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        RiscPlayerInRoom playerA = new RiscPlayerInRoom();
        RiscPlayerInRoom playerB = new RiscPlayerInRoom();
        Map<Integer, RiscPlayerInRoom> clientPlayerMap = new HashMap<>();
        clientPlayerMap.put(1, playerA);
        clientPlayerMap.put(2, playerB);

        RiscServer riscServer = new RiscServer(2, 5);
        try {
            attack1.validateAction(
                    new RiscMap(2, 2, colorList), riscServer);
        } catch (Exception e) {

        }
    }

    @Test
    void attackGetterMethod() {
        Attack attack = new Attack(0, 3, 0, 1, 0);
        assertEquals(attack.getPlayerID(), 0);
        assertEquals(attack.getNumOfUnits(), 3);
        assertEquals(attack.getStartTerritoryID(), 0);
        assertEquals(attack.getEndTerritoryID(), 1);
        assertEquals(attack.getUnitType(), 0);
        attack.getNumOfFoodResourcesNeeded();
        assertEquals(attack.toString(), "Attack{startTerritoryID=0, endTerritoryID=1, unitType=0, numOfFoodResourcesNeeded=3, playerID=0, numOfUnits=3}");
    }

    @Test
    public void executeAction() {
        Attack attack = new Attack();
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        try {
            attack.executeAction(new RiscMap(2, 5, colorList), new RiscPlayerInRoom());
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
    }
}