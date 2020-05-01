package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RiscClientMessageParserTest {

    @Test
    void parseMessageTest() {
        RiscClient client = new RiscCMDClient();
        RiscConsole riscConsole = (RiscConsole) new RiscCMDConsole(client);
        RiscClientMessageParser riscClientMessageParser = new RiscClientMessageParser(client, riscConsole);
        ArrayList<Integer> roomSizeInfo = new ArrayList<>();
        roomSizeInfo.add(2);
        roomSizeInfo.add(5);
        RiscMessage roomSizeMessage = new RiscMessage(RiscMessageType.ROOMSIZE, roomSizeInfo);
        try {
            riscClientMessageParser.parseMessage(roomSizeMessage);
        } catch (Exception e) {
        }

        RiscMessage greetingMessage = new RiscMessage(RiscMessageType.GREETING, 1);
        try {
            riscClientMessageParser.parseMessage(greetingMessage);
        } catch (Exception e) {
        }

        RiscMessage actionMessage = new RiscMessage(RiscMessageType.ACTION, null);
        try {
            riscClientMessageParser.parseMessage(actionMessage);
        } catch (Exception e) {
        }

        RiscMessage winMessage = new RiscMessage(RiscMessageType.WIN, 1);
        try {
            riscClientMessageParser.parseMessage(winMessage);
        } catch (Exception e) {
        }

        RiscMessage lostMessage = new RiscMessage(RiscMessageType.LOST, null);
        try {
            riscClientMessageParser.parseMessage(lostMessage);
        } catch (Exception e) {
        }

        RiscMessage infoMessage = new RiscMessage(RiscMessageType.INFO, "Info");
        try {
            riscClientMessageParser.parseMessage(infoMessage);
        } catch (Exception e) {
        }
    }
}