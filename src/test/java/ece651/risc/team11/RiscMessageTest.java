package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiscMessageTest {

    @Test
    void getType() {
        RiscMessage riscMessage = new RiscMessage(RiscMessageType.GREETING, null);
        assertEquals(riscMessage.getType(), RiscMessageType.GREETING);
    }

    @Test
    void getData() {
        RiscMessage riscMessage = new RiscMessage(RiscMessageType.GREETING, "haha");
        assertEquals(riscMessage.getData(), "haha");
    }
}