package ece651.risc.team11;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int minNumOfPlayers;
    private int maxNumOfPlayers;
    private volatile int roomNumOfPlayers;
    private volatile int curNumOfPlayers;
    private volatile List<RiscPlayerInRoom> playerList;
    private volatile int roomOwnerPosition;
    private GamePhase gamePhase;

    public Room(int minNumOfPlayers, int maxNumOfPlayers) {
        this.minNumOfPlayers = minNumOfPlayers;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.roomNumOfPlayers = 2; //The number of players should be, which is specified by the first player
        this.curNumOfPlayers = 0; //The current number of players in the game
        playerList = new ArrayList<>();
        playerList.add(null);
        playerList.add(null);
    }

    public List<RiscPlayerInRoom> getPlayerList() {
        return playerList;
    }

    public synchronized int addPlayer(RiscPlayer player) {
        for (int i = 0; i < roomNumOfPlayers; i++) {
            if (playerList.get(i) == null) {
                RiscPlayerInRoom playerInGame = new RiscPlayerInRoom(player);
                playerInGame.setColorID(i);
                playerList.set(i, playerInGame);
                curNumOfPlayers++;
                return i;
            }
        }
        return -1;
    }

    public synchronized int removePlayer(int playerNumber) {
        for (int i = 0; i < roomNumOfPlayers; i++) {
            if (playerList.get(i) == null) {
                continue;
            }
            if (playerList.get(i).getPlayerID() == playerNumber) {
                playerList.set(i, null);
                curNumOfPlayers--;
                return i;
            }
        }
        return -1;
    }

    public RiscPlayerInRoom getPlayer(int playerID) {
        for (RiscPlayerInRoom player : playerList) {
            if (player.getPlayerID() == playerID) {
                return player;
            }
        }
        return null;
    }

    public void setPlayerInRoom(List<RiscPlayerInRoom> playerInRoom) {
        this.playerList = playerInRoom;
    }

    public int getRoomNumOfPlayers() {
        return roomNumOfPlayers;
    }

    /*public int getNumOfActivePlayer() {
        int numOfActivePlayer = 0;
        for (RiscPlayerInRoom playerInRoom: this.playerList) {
            if (playerInRoom.isActive())
                numOfActivePlayer += 1;
        }
        return numOfActivePlayer;
    }*/

    public synchronized void setRoomNumOfPlayers(int roomNumOfPlayers) {
        if (roomNumOfPlayers <= maxNumOfPlayers && roomNumOfPlayers >= minNumOfPlayers) {
            while (playerList.remove(null)) {
            }

            if (roomNumOfPlayers > curNumOfPlayers) {
                for (int i = this.curNumOfPlayers; i < roomNumOfPlayers; i++) {
                    playerList.add(null);
                }
            } else {
                for (int i = roomNumOfPlayers; i < this.curNumOfPlayers; i++) {
                    playerList.remove(i);
                }
                curNumOfPlayers = roomNumOfPlayers;
            }
            this.roomNumOfPlayers = roomNumOfPlayers;
        }
    }

    public List<Integer> getPlayerPositionList() {
        ArrayList<Integer> clientIDList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            clientIDList.add(playerList.get(i).getPlayerID());
        }
        return clientIDList;
    }

    public boolean isReady(int playerID) {
        return getPlayer(playerID).isReady();
    }

    public void setReady(int playerID, boolean bool) {
        getPlayer(playerID).setReady(bool);
    }

    public boolean isActive(int playerID) {
        return getPlayer(playerID).isActive();
    }

    public void setActive(int playerID, boolean bool) {
        getPlayer(playerID).setActive(bool);
    }

    public int getCurNumOfPlayers() {
        return curNumOfPlayers;
    }

    public int getRoomOwnerPosition() {
        return roomOwnerPosition;
    }

    public void setRoomOwnerPosition(int roomOwnerPosition) {
        this.roomOwnerPosition = roomOwnerPosition;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }
}