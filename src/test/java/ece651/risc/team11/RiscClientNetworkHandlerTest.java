package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RiscClientNetworkHandlerTest {
    public static InputStream setupMockInput(ArrayList<Object> inputs) throws IOException {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(temp);
        for (Object o : inputs) {
            oos.writeObject(o);
        }
        oos.flush();
        return new ByteArrayInputStream(temp.toByteArray());
    }

    public static OutputStream setupMockOutput() throws IOException {
        return new ByteArrayOutputStream();
    }

    public static Socket setupMockSocket(ArrayList<Object> inputs) throws IOException {
        ServerSocket mockParentServer = mock(ServerSocket.class);
        Socket mockClientSocket = mock(Socket.class);
        when(mockParentServer.accept()).thenReturn(mockClientSocket);
        InputStream inp = setupMockInput(inputs);
        OutputStream out = setupMockOutput();
        when(mockClientSocket.getInputStream()).thenReturn(inp);
        when(mockClientSocket.getOutputStream()).thenReturn(out);
        return mockClientSocket;
    }


    public static RiscClient setupMockClient() {
        return new RiscGUIClient() {
            @Override
            public void displayText(String msg) {

            }

            @Override
            public void sendMessageToServer(Serializable object) {

            }

            @Override
            public RiscConsole getConsole() {
                return null;
            }

            @Override
            public void setReady(boolean ready) {

            }

            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public void setActive(boolean active) {

            }
        };

    }

    @Test
    void makeConnection() {
        RiscClientNetworkHandler riscClientNetworkHandler =
                new RiscClientNetworkHandler(setupMockClient());
        try {
            riscClientNetworkHandler.makeConnection(setupMockSocket(new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendMessageToServer() {
    }
}