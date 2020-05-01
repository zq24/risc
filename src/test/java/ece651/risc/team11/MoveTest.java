package ece651.risc.team11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {


    @Test
    void validateAction() {
        Move upgrade = new Move();
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

    @Test
    void moveMethods() {
        Move move = new Move(0, 1, 0, 1, 0);
        assertEquals(move.getPlayerID(), 0);
        assertEquals(move.getNumOfUnits(), 1);
        assertEquals(move.getStartTerritoryID(), 0);
        assertEquals(move.getEndTerritoryID(), 1);
        assertEquals(move.getUnitType(), 0);
        assertEquals(move.toString(), "Move{startTerritoryID=0, endTerritoryID=1, unitType=0, numOfFoodResourcesNeeded=0, playerID=0, numOfUnits=1}");
    }

    @Test
    public void executeAction() {
        Move move = new Move();
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        try {
            move.executeAction(new RiscMap(2, 5, colorList), new RiscPlayerInRoom());
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
    }
}