package ece651.risc.team11;

import java.util.List;

public interface RiscConsole {
    /**
     * Display message to the user.
     *
     * @param message - message to be displayed
     */
    void displayText(String message);

    /**
     * Ask user to decide size of the room. The size should be in the range of
     * minNumOfPlayers and maxNumOfPlayers.
     *
     * @param minNumOfPlayers minimum number of players in a game.
     * @param maxNumOfPlayers maximum number of players in a game.
     */
    void decideRoomSize(int minNumOfPlayers, int maxNumOfPlayers);

    void displayMap(RiscMap riscMap, int playerID);

    void setPlayerReady(List<RiscPlayerInRoom> ready);

    /**
     * Ask user to enter their actions for this round.
     */
    void enterAllActions();

    /**
     * Show the user when somebody won the game.
     *
     * @param playerID - winner's playerID
     */
    void showWin(int playerID);

    void updateMap(RiscMap riscMap);

    void setPlayerInfo(RiscPlayerInRoom player);

    void exitARoom();
}