package ece651.risc.team11;

import ece651.risc.team11.GUIConsole.RiscGUIConsole;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * This class represents the player.
 */
public class RiscGUIClient extends Application implements RiscClient {
    private String serverIP = ConfigLoader.getProperty("ip");
    private int serverPort = Integer.parseInt(ConfigLoader.getProperty("port"));
    private RiscClientNetworkHandler clientNetworkHandler;
    private RiscConsole console;
    private boolean isReady;
    private boolean isActive;

    public RiscGUIClient() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.clientNetworkHandler = new RiscClientNetworkHandler(this);
        //this.console = new RiscGraphicalMapConsole(this, stage, new TextField(), new TextArea());
        this.console = new RiscGUIConsole(this, stage);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
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

    public void displayText(String msg) {
        console.displayText(msg);
    }

    public void sendMessageToServer(Serializable object) {
        clientNetworkHandler.sendMessageToServer(object);
    }

    public RiscConsole getConsole() {
        return console;
    }

    @Override
    public void connect() {
        try {
            clientNetworkHandler.makeConnection(new Socket(serverIP, serverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}