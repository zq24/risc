package ece651.risc.team11;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * This class represents the player.
 */
public class RiscCMDClient implements RiscClient {
    private String serverIP = ConfigLoader.getProperty("ip");
    private int serverPort = Integer.parseInt(ConfigLoader.getProperty("port"));
    private RiscClientNetworkHandler clientNetworkHandler;
    private RiscConsole console;
    private boolean isReady;
    private boolean isActive;

    public RiscCMDClient() {
        this.clientNetworkHandler = new RiscClientNetworkHandler(this);
        this.console = new RiscCMDConsole(this);
        try {
            clientNetworkHandler.makeConnection(new Socket(serverIP, serverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void connect() {

    }

    public void displayText(String msg) {
        console.displayText(msg);
    }

    public void sendMessageToServer(Serializable object) {
        clientNetworkHandler.sendMessageToServer(object);
    }

    public RiscConsole getConsole() {
        return console;
    }

    public static void main(String[] args) {
        RiscClient riscClient = new RiscCMDClient();
    }

}