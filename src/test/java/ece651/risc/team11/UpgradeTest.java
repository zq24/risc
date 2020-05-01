package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeTest {

    @Test
    void upgradeConstructors() {
        Action upgrade = new Upgrade(0, 1, 0, 1, 0);
        Upgrade copy = new Upgrade(upgrade, 0, 1, 0);
        Upgrade empty = new Upgrade();
    }

    @Test
    void upgradeMethods() {
        Upgrade upgrade = new Upgrade(0, 1, 0, 1, 0);
        assertEquals(upgrade.getEndUnitType(), 1);
        assertEquals(upgrade.getStartUnitType(), 0);
        assertEquals(upgrade.getUpgradeTerritoryID(), 0);
        assertEquals(upgrade.toString(), "Upgrade{startUnitType=0, endUnitType=1, upgradeTerritoryID=0, numOfTechResourceNeeded=0, playerID=0, numOfUnits=1}");
        upgrade.getNumOfTechResourceNeeded();
    }

    @Test
    public void executeAction() {
        Upgrade upgrade = new Upgrade();
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        try {
            upgrade.executeAction(new RiscMap(2, 5, colorList), new RiscPlayerInRoom());
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        upgrade.calculateCost();
        upgrade.costOutOfBoundary(100, new RiscPlayerInRoom());

    }

    @Test
    void validateAction() {
        Upgrade upgrade = new Upgrade();
        ArrayList<Integer> colorList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        RiscPlayer playerA = new RiscPlayer();
        RiscPlayer playerB = new RiscPlayer();
        Map<Integer, RiscPlayer> clientPlayerMap = new HashMap<>();
        clientPlayerMap.put(1, playerA);
        clientPlayerMap.put(2, playerB);
        RiscServer riscServer = new RiscServer(2, 5);
        riscServer.setClientPlayerMap(clientPlayerMap);
        Room room = new Room(2, 5);
        RiscPlayerInRoom playerInRoom1 = new RiscPlayerInRoom();
        playerInRoom1.setPlayerID(0);
        RiscPlayerInRoom playerInRoom2 = new RiscPlayerInRoom();
        playerInRoom2.setPlayerID(1);
        List<RiscPlayerInRoom> players = new ArrayList<>();
        players.add(playerInRoom1);
        players.add(playerInRoom2);
        room.setPlayerInRoom(players);
        riscServer.addRoom(room);
        upgrade.setPlayerID(0);
        upgrade.validateAction(new RiscMap(2, 2, colorList), riscServer);
    }
}