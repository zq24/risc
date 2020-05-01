package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RiscMapTest {

    @Test
    void getTerritoryAdjList() {
        ArrayList<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            colorList.add(i);
        }
        RiscMap riscMap = new RiscMap(1, 1, colorList);
        assertEquals(riscMap.getTerritoryAdjList().get(0).size(), 0);

        for (int i = 2; i < 20; i++) {
            for (int j = 1; j < 5; j++) {
                riscMap = new RiscMap(j, i, colorList);
                for (int v : riscMap.getTerritoryAdjList().keySet()) {
                    assertNotEquals(riscMap.getTerritoryAdjList().get(v).size(), 0);
                }
            }
        }
        try {
            riscMap.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}