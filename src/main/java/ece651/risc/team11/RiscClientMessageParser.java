package ece651.risc.team11;

import java.util.ArrayList;
import java.util.List;

/**
 * This class parses messages that were sent from server based on the
 * type of message and deal with it accordingly.
 */
public class RiscClientMessageParser {
    private RiscClient client;
    private RiscConsole console;
    private int playerID;

    public RiscClientMessageParser(RiscClient client, RiscConsole console) {
        this.client = client;
        this.console = console;
    }

    /**
     * Parse the message received from RiscServer and perform corresponding operations.
     *
     * @param message message received from RiscServer
     */
    public void parseMessage(RiscMessage message) {
        RiscMessageType type = message.getType();
        switch (type) {
            case ROOMSIZE:
                @SuppressWarnings("unchecked")
                ArrayList<Integer> playerNumberLimits = (ArrayList<Integer>) message.getData();
                console.decideRoomSize(playerNumberLimits.get(0), playerNumberLimits.get(1));
                break;
            case GREETING:
                client.setActive(true);
                /*if (message.getData() != null) {
                    console.displayText(String.format("I am player%d.", (int) message.getData()));
                    playerID = (int) message.getData();
                }

                 */
                client.setReady(true);
                //console.displayText("Waiting for others to join the game...");
                break;
            case ACTION:
                console.displayMap((RiscMap) message.getData(), playerID);
                break;

            case ERROR:
                console.displayText("You have invalid actions: \n"
                        + message.getData() + "\nRe-enter new actions.");
                break;
            case WIN:
                console.showWin((int) message.getData());
                break;
            case LOST:
                client.setActive(false);
                console.displayText("You lost... but you can still watch the game.");
                break;
            case INFO:
                console.displayText((String) message.getData());
                break;
            case PLAYER:
                console.setPlayerInfo((RiscPlayerInRoom) message.getData());
                break;
            case READY:
                @SuppressWarnings("unchecked")
                List<RiscPlayerInRoom> playerInRoomList = (List<RiscPlayerInRoom>) message.getData();
                console.setPlayerReady(playerInRoomList);
                break;
            case EXITROOM:
                console.exitARoom();
                break;
            default:
                break;
        }
    }
}