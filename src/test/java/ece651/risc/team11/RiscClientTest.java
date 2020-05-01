package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class RiscClientTest {

    @Test
    void makeConnection() {
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
            }
        }));
        RiscClient riscClient = new RiscGUIClient();
    }

    @Test
    void sendMessageToServer() {
    }

    @Test
    void isReady() {
        RiscGUIClient riscClient = null;
        try {
            riscClient = new RiscGUIClient();
        } catch (Exception e) {
        }
        riscClient.setReady(true);
        assertTrue(riscClient.isReady());
    }

    @Test
    void setReady() {
        RiscClient riscClient = null;
        try {
            riscClient = new RiscGUIClient();
        } catch (Exception e) {
        }
        riscClient.setReady(true);
    }

    @Test
    void isActive() {
        RiscClient riscClient = null;
        try {
            riscClient = new RiscGUIClient();
        } catch (Exception e) {
        }
        riscClient.setActive(true);
        assertTrue(riscClient.isActive());
    }

    @Test
    void setActive() {
        RiscClient riscClient = null;
        try {
            riscClient = new RiscGUIClient();
        } catch (Exception e) {
        }
        riscClient.setActive(true);
    }
}