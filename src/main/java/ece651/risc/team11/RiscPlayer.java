package ece651.risc.team11;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RiscPlayer {
    private transient Socket socket;
    private transient ObjectOutputStream objectOutputStream;
    private int playerID;

    public RiscPlayer() {
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}