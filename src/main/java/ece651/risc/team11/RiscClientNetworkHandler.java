package ece651.risc.team11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class RiscClientNetworkHandler {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private RiscClient client;

    public RiscClientNetworkHandler(RiscClient client) {
        this.client = client;
    }

    /**
     * This method would make connection with the server.
     */
    public void makeConnection(Socket socket) {
        try {
            this.socket = socket;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Thread readerThread = new Thread(new ServerHandler(
                    new RiscClientMessageParser(client, client.getConsole())));
            readerThread.start();
        } catch (IOException e) {
            client.displayText("Connect to server failed...");
        }
    }

    /**
     * This method sends messages from client to server.
     *
     * @param object message.
     */
    public void sendMessageToServer(Serializable object) {
        try {
            objectOutputStream.writeObject(object);
            objectOutputStream.reset();
        } catch (IOException e) {
            client.displayText("Unable to send to server...");
        }
    }

    class ServerHandler implements Runnable {
        private RiscClientMessageParser clientMessageParser;

        public ServerHandler(RiscClientMessageParser clientMessageParser) {
            this.clientMessageParser = clientMessageParser;
        }

        @Override
        public void run() {
            try {
                RiscMessage message;
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                while ((message = (RiscMessage) objectInputStream.readObject()) != null) {
                    //May need many subclasses of message? instead of from their type
                    clientMessageParser.parseMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                client.displayText("Server down...");
            }

        }
    }
}