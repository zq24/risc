package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RiscServerTest {

    @Test
    void serverMethods() {
        RiscServer server = new RiscServer(2, 5);
        server.getMap();
        List<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new Upgrade());
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack());
        server.setAllAttackActions(attacks);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move());
        server.getClientPlayerMap();
        server.getAllAttackActions();
        List<Upgrade> newUpgrade = new ArrayList<>();
        newUpgrade.add(new Upgrade());
        List<Attack> newAttack = new ArrayList<>();
        newAttack.add(new Attack());
        List<Move> newMove = new ArrayList<>();
        newMove.add(new Move());
        server.addAttackActions(newAttack);
        server.getUpdateFoodAmount();
        server.getUpdateTechAmount();
        RiscPlayer player = new RiscPlayer();
        server.getClientPlayerMap().put(0, player);

        RiscPlayer playerA = new RiscPlayer();
        RiscPlayer playerB = new RiscPlayer();
        Map<Integer, RiscPlayer> clientPlayerMap = new HashMap<>();
        clientPlayerMap.put(0, playerA);
        clientPlayerMap.put(1, playerB);
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
        riscServer.resetReadyState();
        // riscServer.restartGame();
        riscServer.setClientReadyAndCheck(0, true);
        server.setGamePhase(GamePhase.WAIT_TO_START);
        server.getIDSet();
    }
}