package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RiscServerMessageParserTest {

    @Test
    void parserMethods() {
        RiscServerMessageParser parser =
                new RiscServerMessageParser(new RiscServer(2, 5), Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), 0);

        RiscMessage roomSizeMessage = new RiscMessage(RiscMessageType.ROOMSIZE, 2);
        try {
            parser.parseMessage(0, roomSizeMessage);
        } catch (Exception e) {
        }

        RiscMessage readyMessage = new RiscMessage(RiscMessageType.READY, true);
        try {
            parser.parseMessage(0, readyMessage);
        } catch (Exception e) {

        }
    }

    @Test
    void otherTest() {
        List<Instruction> instructions = new ArrayList<>();
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
        room.setActive(0, true);
        riscServer.addRoom(room);
        riscServer.initializeMap();
        RiscServerMessageParser parser =
                new RiscServerMessageParser(riscServer, Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), 0);
        Instruction instruction3 = new Instruction("alliance", "1", "2", "3", "4", "5", "1");
        instructions.add(instruction3);
        Instruction instruction4 = new Instruction("unknown", "1", "2", "3", "4", "5", "6");
        instructions.add(instruction4);
        Instruction instruction1 = new Instruction("upgrade", "0", "0", "3", "0", "1", "6");
        instructions.add(instruction1);
        Instruction instruction8 = new Instruction("upgrade", "1", "0", "3", "0", "1", "6");
        instructions.add(instruction8);
        Instruction instruction9 = new Instruction("upgrade", "2", "0", "3", "0", "1", "6");
        instructions.add(instruction9);
        Instruction instruction10 = new Instruction("upgrade", "3", "0", "3", "0", "1", "6");
        instructions.add(instruction10);
        Instruction instruction = new Instruction("move", "0", "1", "3", "0", "0", "6");
        instructions.add(instruction);
        Instruction instruction5 = new Instruction("move", "0", "2", "3", "0", "0", "6");
        instructions.add(instruction5);
        Instruction instruction6 = new Instruction("move", "0", "3", "3", "0", "0", "6");
        instructions.add(instruction6);
        Instruction instruction7 = new Instruction("move", "1", "3", "3", "0", "0", "6");
        instructions.add(instruction7);

        Instruction instruction2 = new Instruction("attack", "0", "2", "3", "0", "0", "6");
        instructions.add(instruction2);

        Instruction instruction11 = new Instruction("attack", "0", "1", "3", "0", "0", "6");
        instructions.add(instruction11);

        Instruction instruction12 = new Instruction("attack", "0", "3", "3", "0", "0", "6");
        instructions.add(instruction12);


        List<Integer> colorList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            colorList.add(i);
        }

        parser.convertInstructionToAction(instructions, riscServer.getMap(), new ArrayList<Attack>(), new ArrayList<Alliance>());


        Attack attack1 = new Attack(0,
                1,
                0,
                2,
                0);
        Attack attack2 = new Attack(1,
                1,
                2,
                0,
                0);
        List<Attack> attacks = new ArrayList<>();
        attacks.add(attack1);
        attacks.add(attack2);
        parser.executeAttackActions(attacks, riscServer.getMap());

        parser.updateResources(riscServer.getMap());
        parser.addUnitToAllTerritories(riscServer.getMap());
        parser.checkTurnResult(riscServer.getMap());
        List<Action> actions = new ArrayList<>();
        Alliance alliance = new Alliance(0, 0, 1);
        Move move = new Move(1,
                1,
                0,
                1,
                0);
        Move move2 = new Move(0,
                1,
                2,
                3,
                0);
        Upgrade upgrade = new Upgrade(0,
                1,
                0,
                1,
                0);
        actions.add(attack1);
        actions.add(attack2);
        actions.add(alliance);
        actions.add(move);
        actions.add(move2);
        actions.add(upgrade);
        parser.validateAllActions(actions, true);

    }
}