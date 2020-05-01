package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RoomTest {
    @Test
    void roomTest() {
        Room room = new Room(2, 5);
        RiscPlayer riscPlayer = new RiscPlayer();
        room.addPlayer(riscPlayer);
        room.removePlayer(1);
        RiscPlayerInRoom playerInRoom1 = new RiscPlayerInRoom();
        playerInRoom1.setPlayerID(0);
        RiscPlayerInRoom playerInRoom2 = new RiscPlayerInRoom();
        playerInRoom2.setPlayerID(1);
        List<RiscPlayerInRoom> players = new ArrayList<>();
        players.add(playerInRoom1);
        players.add(playerInRoom2);
        room.setPlayerInRoom(players);
        RiscPlayer riscPlayer2 = new RiscPlayer();
        room.addPlayer(riscPlayer2);

        room.getPlayerPositionList();
        room.getPlayerList();
        room.getPlayer(0);
        room.getCurNumOfPlayers();
        room.getGamePhase();
        room.getRoomNumOfPlayers();
        room.getRoomOwnerPosition();
        room.setActive(0, false);
        room.setRoomNumOfPlayers(2);
        room.setRoomOwnerPosition(2);
        room.removePlayer(1);
    }
}