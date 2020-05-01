package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void getColor() {
        Territory territory = new NormalTerritory();
        assertEquals(territory.getColor(), 0);
    }

    @Test
    void setColor() {
        Territory territory = new NormalTerritory();
        territory.setColor(2);
        assertEquals(territory.getColor(), 2);
    }

    @Test
    void getOwnerID() {
        Territory territory = new NormalTerritory();
        assertEquals(territory.getOwnerID(), 0);
    }

    @Test
    void setOwnerID() {
        Territory territory = new NormalTerritory();
        territory.setOwnerID(100);
        assertEquals(territory.getOwnerID(), 100);
    }

    @Test
    void getX() {
        Territory territory = new NormalTerritory(1, 2);
        assertEquals(territory.getX(), 1);
    }

    @Test
    void getY() {
        Territory territory = new NormalTerritory(1, 2);
        assertEquals(territory.getY(), 2);
    }

    @Test
    void setX() {
        Territory territory = new NormalTerritory();
        territory.setX(10);
        assertEquals(territory.getX(), 10);
    }

    @Test
    void setY() {
        Territory territory = new NormalTerritory();
        territory.setY(11);
        assertEquals(territory.getY(), 11);
    }

    @Test
    void getNumOfUnits() {
        Territory territory = new NormalTerritory();
        assertEquals(territory.getNumOfUnitsForType(0), 0);
    }

    @Test
    void addUnit() {
        Territory territory = new NormalTerritory();
        int size = territory.addUnitForType(1, 1, 1);
        assertEquals(size, 1);
    }

    @Test
    void removeUnit() {
        Territory territory = new NormalTerritory();
        List<Unit> units = new ArrayList<>();
        units.add(new NormalUnit(0));
        territory.addUnitForType(0, 1, 0);
        try {
            territory.removeUnitForType(0, 1, 1);
            territory.removeUnitForType(0, -1, 1);
            territory.removeUnitForType(0, 2, 1);

        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        territory.addUnitForType(0, 1, 0);
        try {
            territory.removeUnitForType(0, 1);
            territory.removeUnitForType(0, -1);
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        territory.addUnitForType(0, 1, 0);
        territory.removeAllUnitsOfOwner(0);
        try {
            territory.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        List<Unit> retreateUnit = new ArrayList<>();
        retreateUnit.add(new NormalUnit(0));
        territory.addRetreatUnits(retreateUnit);
    }

    @Test
    void setUnits() {
        Territory territory = new NormalTerritory();
        List<Unit> units = new ArrayList<>();
        units.add(new NormalUnit(0));
        territory.setUnitsForType(0, units);
    }

    @Test
    void getUnits() {
        Territory territory = new NormalTerritory();
        assertEquals(territory.getUnitsForType(0).size(), 0);
    }

    @Test
    void territoryMethods() {
        Territory territory = new NormalTerritory();
        assertEquals(territory.getTechResourceProduction(), 50);
        assertEquals(territory.getFoodResourceProduction(), 50);
        assertEquals(Territory.getTerritorySize(), 5);
        territory.getUnitsForAllType();
        territory.getUnitTypes();
        List<Unit> units = new ArrayList<>();
        units.add(new NormalUnit(0));
        territory.setAttackerUnitsForAllType(units);
    }

    @Test
    void generateUnitForType() {
        Territory territory = new NormalTerritory();
        territory.generateUnitForType(0, 1, 1);
        territory.generateUnitForType(1, 1, 1);
        territory.generateUnitForType(2, 1, 1);
        territory.generateUnitForType(3, 1, 1);
        territory.generateUnitForType(4, 1, 1);
        territory.generateUnitForType(5, 1, 1);
        territory.generateUnitForType(6, 1, 1);
        territory.generateUnitForType(7, 1, 1);
    }
}