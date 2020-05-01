package ece651.risc.team11;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RiscCMDConsoleTest {

    @Test
    void riscCMDConsoleEnterAllInstructionsPlayerActiveTest() {
        String input = "  MoVe  \n0\n1\n1\n" +
                "k\n" +
                "A\n0\n2\n1\n" +
                "A\na\n0\na\n2\na\n1\n" +
                "u\n0\n1\n0\n1\n" +
                "d\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
        riscClient.setActive(true);
        try {
            riscCMDConsole.enterAllActions();
        } catch (Exception e) {
        }
    }

    @Test
    void riscCMDConsoleEnterAllInstructionsPlayerNotActiveTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("Move".getBytes());
        System.setIn(in);
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
        riscCMDConsole.enterAllActions();
    }

    @Test
    void riscCMDConsoleDecideRoomSize() {
        String input = "1\n" +
                "6\n" +
                "3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
        try {
            riscCMDConsole.decideRoomSize(2, 5);
        } catch (Exception e) {
        }
    }

    @Test
    void riscCMDConsoleSendReady() {
        String input = "no\n" +
                "haha\n" +
                "yes\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
    }

    @Test
    void riscCMDConsoleShowWin() {
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
        riscCMDConsole.showWin(1);
    }

    @Test
    void riscCMDConsoleUpdateMap() {
        RiscClient riscClient = new RiscGUIClient();
        RiscCMDConsole riscCMDConsole = new RiscCMDConsole(riscClient);
        ArrayList<String> colorList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            colorList.add(String.format("%d", i));
        }

        List<Integer> playerID = new ArrayList<>();
        playerID.add(1);
        playerID.add(2);
        RiscMap riscMap = new RiscMap(1, 1, playerID);
        riscCMDConsole.updateMap(riscMap);
        riscCMDConsole.displayMap(riscMap, 1);
    }
}