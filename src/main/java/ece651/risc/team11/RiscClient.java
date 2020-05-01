package ece651.risc.team11;

import java.io.Serializable;

public interface RiscClient {
    void displayText(String msg);

    void sendMessageToServer(Serializable object);

    RiscConsole getConsole();

    void setReady(boolean ready);

    boolean isActive();

    void setActive(boolean active);

    void connect();
}