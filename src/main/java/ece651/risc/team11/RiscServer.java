package ece651.risc.team11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class represents the game server.
 */

public class RiscServer {
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Random rand = new Random();
    private volatile Map<Integer, RiscPlayer> clientPlayerMap;
    private volatile ArrayList<RiscMessage> messages; //store received message in each turn, need to be cleared after handling these messages
    private int minNumOfPlayers;
    private int maxNumOfPlayers;
    private boolean serverUp;
    private volatile RiscMap map;
    private volatile List<Room> rooms;

    private static final int numOfTerritoriesPerPlayer = 2;
    private static final int numOfUnits = 3;
    private static final int updateTechAmount = 50;
    private static final int updateFoodAmount = 50;

    /**
     * This field saves all the attack orders of all players for one turn.
     */
    private volatile List<Attack> allAttackActions;

    private volatile List<Alliance> allAllianceActions;

    public RiscServer(int min, int max) {
        this.maxNumOfPlayers = max;
        this.minNumOfPlayers = min;
        this.clientPlayerMap = new HashMap<>();
        this.messages = new ArrayList<>();
        this.allAttackActions = new ArrayList<>();
        this.allAllianceActions = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.rooms.add(new Room(minNumOfPlayers, maxNumOfPlayers));
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("Starts up the server at localhost:" + serverSocket.getLocalPort());
            serverUp = true;
            while (serverUp) {
                Socket clientSocket = serverSocket.accept();
                addConnection(clientSocket);
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error in starting up the server at localhost:" + port);
        }
    }

    //return a client number, convenient for sending message to it...
    private synchronized void addConnection(Socket clientSocket) {
        Room room = rooms.get(0);
        if (room.getCurNumOfPlayers() < room.getRoomNumOfPlayers()) {

            int clientNumber;
            do {
                clientNumber = rand.nextInt();
            } while (clientPlayerMap.containsKey(clientNumber));

            RiscPlayer player = new RiscPlayer();
            clientPlayerMap.put(clientNumber, player);
            clientPlayerMap.get(clientNumber).setPlayerID(clientNumber);
            try {
                player.setSocket(clientSocket);
            } catch (IOException e) {
                System.out.println("Error in creating an ObjectInputStream for the client at " +
                        clientSocket.getRemoteSocketAddress());
                e.printStackTrace();
                clientPlayerMap.remove(clientNumber);
                return;
            }

            room.addPlayer(clientPlayerMap.get(clientNumber));


            logger.info("Establishes a connection with a client at "
                    + clientSocket.getRemoteSocketAddress());
            if (room.getCurNumOfPlayers() == 1) {
                assignRoomOwner(clientNumber);
            }
            sendMessageToClient(clientNumber,
                    new RiscMessage(RiscMessageType.GREETING, clientNumber));
            // Create a thread for receiving messages from this client
            Thread t = new Thread(
                    new ClientHandler(clientSocket,
                            new RiscServerMessageParser(this, logger, clientNumber),
                            clientNumber));
            t.start();
        } else {
            System.out.println(
                    "Server is full: cannot establish a connection with a client at " + clientSocket.getRemoteSocketAddress());
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(
                        "Error in closing the client socket at " + clientSocket.getRemoteSocketAddress());
            }
        }
    }

    private synchronized void removeConnection(Socket clientSocket, int clientID) {
        Room room = rooms.get(0);
        if (room.getCurNumOfPlayers() > 0) {
            exitRoom(clientID);
            clientPlayerMap.remove(clientID);
            broadcastMessage(new RiscMessage(RiscMessageType.READY, getPlayerListInRoom()));
            logger.info(clientSocket.getRemoteSocketAddress() + " leaves the game.");
            restartGame();
        }
    }

    public void setNumOfPlayers(int gameNumOfPlayers) {
        Room room = rooms.get(0);
        room.setRoomNumOfPlayers(gameNumOfPlayers);
        if (room.getCurNumOfPlayers() < clientPlayerMap.size()) {
            Set<Integer> toBeRemoved = new HashSet<>();
            for (int playerID : clientPlayerMap.keySet()) {
                if (room.getPlayer(playerID) == null) {
                    toBeRemoved.add(playerID);
                }
            }
            for (int playerID : toBeRemoved) {
                sendMessageToClient(playerID, new RiscMessage(
                        RiscMessageType.EXITROOM, null));
            }
        }
    }

    public void exitRoom(int clientID) {
        Room room = rooms.get(0);
        int removedPosition = room.removePlayer(clientID);
        if (removedPosition == room.getRoomOwnerPosition()) {
            for (int i = 0; i < room.getPlayerList().size(); i++) {
                if (room.getPlayerList().get(i) != null) {
                    room.setRoomOwnerPosition(i);
                    assignRoomOwner(room.getPlayerList().get(i).getPlayerID());
                    break;
                }
            }
        }
    }

    private void assignRoomOwner(int clientNumber) {
        ArrayList<Integer> maxMinPair = new ArrayList<>();
        maxMinPair.add(minNumOfPlayers);
        maxMinPair.add(maxNumOfPlayers);
        sendMessageToClient(clientNumber,
                new RiscMessage(RiscMessageType.ROOMSIZE, maxMinPair));
    }

    public void sendMessageToClient(int clientNumber, Serializable object) {
        try {
            clientPlayerMap.get(clientNumber).getObjectOutputStream().writeObject(object);
            clientPlayerMap.get(clientNumber).getObjectOutputStream().reset();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to send to client...");
        }
    }

    public void broadcastMessage(Serializable object) {
        for (int key : clientPlayerMap.keySet()) {
            sendMessageToClient(key, object);
        }
    }

    public Set<Integer> getIDSet() {
        return clientPlayerMap.keySet();
    }

    public void setGamePhase(GamePhase gamePhase) {
        rooms.get(0).setGamePhase(gamePhase);
    }

    /*public int getGameNumOfPlayers() {
        return this.gameNumOfPlayers;
    }*/

    public RiscMap getMap() {
        return this.map;
    }

    public void setClientPlayerMap(Map<Integer, RiscPlayer> clientPlayerMap) {
        this.clientPlayerMap = clientPlayerMap;
    }

    public void initializeMap() {
        Room room = rooms.get(0);
        setAllAttackActions(new ArrayList<>());
        setAllAllianceActions(new ArrayList<>());

        //match color and client ID
        this.map = new RiscMap(numOfTerritoriesPerPlayer,
                room.getRoomNumOfPlayers(), room.getPlayerPositionList());
        for (Territory territory : map.getTerritoryList()) {
            territory.addUnitForType(0, numOfUnits, territory.getOwnerID());
        }
    }

    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public synchronized boolean setClientReadyAndCheck(int playerID, boolean bool) {
        Room room = rooms.get(0);
        room.setReady(playerID, bool);
        if (room.getCurNumOfPlayers() < room.getRoomNumOfPlayers()) {
            return false;
        }
        for (int key : clientPlayerMap.keySet()) {
            if (!room.isReady(key)) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean setClientReadyAndCheckDuringGame(int playerID, boolean bool) {
        Room room = rooms.get(0);
        room.setReady(playerID, bool);
        if (room.getCurNumOfPlayers() < room.getRoomNumOfPlayers()) {
            return false;
        }
        for (int key : clientPlayerMap.keySet()) {
            if (!room.isActive(key)) {
                continue;
            }

            if (!room.isReady(key)) {
                return false;
            }
        }
        return true;
    }

    public void resetReadyState() {
        Room room = rooms.get(0);
        for (int key : clientPlayerMap.keySet()) {
            room.setReady(key, false);
        }
    }

    public void restartGame() {
        Room room = rooms.get(0);
        if (room.getGamePhase() != GamePhase.WAIT_TO_START) {
            room.setGamePhase(GamePhase.WAIT_TO_START);
            for (int key : clientPlayerMap.keySet()) {
                room.setReady(key, false);
                room.setActive(key, false);
                room.getPlayer(key).resetAllResource();
            }
            broadcastMessage(new RiscMessage(RiscMessageType.INFO, "Game has been restarted."));
            broadcastMessage(new RiscMessage(RiscMessageType.GREETING, null));
        }
    }


    private class ClientHandler implements Runnable {
        private final int clientID;
        private final Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private final RiscServerMessageParser serverMessageParser;

        public ClientHandler(Socket clientSocket,
                             RiscServerMessageParser serverMessageParser,
                             int clientID) {
            this.clientSocket = clientSocket;
            try {
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                logger.warning("Error in creating an ObjectInputStream for the client at "
                        + clientSocket.getRemoteSocketAddress());
                e.printStackTrace();
                removeConnection(clientSocket, clientID);
            }
            this.serverMessageParser = serverMessageParser;
            this.clientID = clientID;
        }

        //What if constructor has an exception? objectInputStream may not be initialized
        public void run() {
            try {
                RiscMessage message;
                while ((message = (RiscMessage) objectInputStream.readObject()) != null) {
                    logger.info("Message received from "
                            + clientSocket.getRemoteSocketAddress());
                    serverMessageParser.parseMessage(clientID, message);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.warning("Error in receiving messages from the client at "
                        + clientSocket.getRemoteSocketAddress());
                removeConnection(clientSocket, clientID);
            }
        }
    }

    public int getUpdateTechAmount() {
        return updateTechAmount;
    }

    public int getUpdateFoodAmount() {
        return updateFoodAmount;
    }


    public List<Attack> getAllAttackActions() {
        return allAttackActions;
    }

    public synchronized void addAttackActions(List<Attack> attackList) {
        this.allAttackActions.addAll(attackList);
    }

    public void setAllAttackActions(List<Attack> allAttackActions) {
        this.allAttackActions = allAttackActions;
    }

    public synchronized void addAllianceAction(List<Alliance> allianceList) {
        this.allAllianceActions.addAll(allianceList);
    }

    public void setAllAllianceActions(List<Alliance> allAllianceActions) {
        this.allAllianceActions = allAllianceActions;
    }

    public List<Alliance> getAllAllianceActions() {
        return this.allAllianceActions;
    }

    public Map<Integer, RiscPlayer> getClientPlayerMap() {
        return clientPlayerMap;
    }

    public List<RiscPlayerInRoom> getPlayerListInRoom() {
        Room room = rooms.get(0);
        return room.getPlayerList();
    }

    public RiscPlayerInRoom getPlayerInRoom(int playerID) {
        return rooms.get(0).getPlayer(playerID);
    }

    public void addRoom(Room room) {
        rooms = new ArrayList<>();
        this.rooms.add(room);
    }

    public static void main(String[] args) {
        RiscServer riscServer = new RiscServer(2, 5);
        riscServer.start(Integer.parseInt(ConfigLoader.getProperty("port")));
    }
}