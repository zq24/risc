package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllianceTest {
    @Test
    void validateAction() {
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
            }
        }));
        Alliance alliance = new Alliance(0);
        Alliance alliance2 = new Alliance(0, 1, 1);

        List<Integer> colorList = new ArrayList<>();
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
        alliance.setPlayerID(0);
        alliance.validateAction(
                new RiscMap(2, 2, colorList), riscServer);
        alliance2.setPlayerID(0);
        alliance2.validateAction(
                new RiscMap(2, 2, colorList), riscServer);
    }

    @Test
    public void executeAction() {
        Alliance alliance = new Alliance(0);
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorList.add(i);
        }
        alliance.executeAction(new RiscMap(2, 5, colorList), new RiscPlayerInRoom());
        alliance.setAllianceId(1);
        alliance.getAllianceId();
    }

}