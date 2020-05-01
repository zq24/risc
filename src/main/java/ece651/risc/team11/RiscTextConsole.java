package ece651.risc.team11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RiscTextConsole implements RiscConsole {
    private final List<String> colorList = new ArrayList<>
            (Arrays.asList("Red", "Green", "Blue", "Purple", "Pink"));
    private RiscClient client;
    private StringAsker asker;
    private PrintStream printStream;
    //  private int playerID;

    public RiscTextConsole(RiscClient client, InputStream inputStream, OutputStream outputStream) {
        this.client = client;
        asker = new StringAsker(inputStream, outputStream);
        printStream = new PrintStream(outputStream, true);
    }

    @Override
    public void displayText(String message) {

    }

    @Override
    public void decideRoomSize(int minNumOfPlayers, int maxNumOfPlayers) {

    }

    @Override
    public void displayMap(RiscMap riscMap, int playerID) {

    }

    @Override
    public void setPlayerReady(List<RiscPlayerInRoom> ready) {

    }

    @Override
    public void enterAllActions() {

    }

    @Override
    public void showWin(int playerID) {

    }

    @Override
    public void updateMap(RiscMap riscMap) {

    }

    @Override
    public void setPlayerInfo(RiscPlayerInRoom player) {

    }

    @Override
    public void exitARoom() {

    }
}