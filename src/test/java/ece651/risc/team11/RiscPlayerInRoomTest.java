package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RiscPlayerInRoomTest {

    @Test
    void riscPlayerMethods() {
        RiscPlayerInRoom riscPlayerInRoom = new RiscPlayerInRoom();
        riscPlayerInRoom.setActive(true);
        riscPlayerInRoom.setColorID(1);
        riscPlayerInRoom.setPlayerID(0);
        riscPlayerInRoom.setReady(true);
        assertTrue(riscPlayerInRoom.isReady());
        assertTrue(riscPlayerInRoom.isActive());
        assertEquals(riscPlayerInRoom.getColorID(), 1);
        assertEquals(riscPlayerInRoom.getPlayerID(), 0);
        assertEquals(riscPlayerInRoom.getFoodResource().size(), 300);
        assertEquals(riscPlayerInRoom.getTechnologyResource().size(), 300);
        assertEquals(riscPlayerInRoom.numOfFoodResource(), 300);
        assertEquals(riscPlayerInRoom.numOfTechResource(), 300);
        try {
            riscPlayerInRoom.removeFoodResource(50);
            riscPlayerInRoom.removeTechResource(50);
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        assertEquals(riscPlayerInRoom.numOfFoodResource(), 250);
        assertEquals(riscPlayerInRoom.numOfTechResource(), 250);
    }
}