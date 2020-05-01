package ece651.risc.team11;

import java.io.Serializable;

/**
 * This class represents the media that the server and client will be used to communicate.
 */
public class RiscMessage implements Serializable {
    private static final long serialVersionUID = -9138385504565085818L;

    /**
     * This field indicates the type of the message.
     */
    private RiscMessageType type;

    /**
     * This field represents the actual payload of the message.
     */
    private Object data;

    public RiscMessage(RiscMessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public RiscMessageType getType() {
        return this.type;
    }

    public Object getData() {
        return this.data;
    }
}