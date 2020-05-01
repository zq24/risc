package ece651.risc.team11;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents territory
 */
public abstract class Territory implements Serializable, Cloneable {
    private static final int TECH_RESOURCE_PRODUCTION = 50;
    private static final int FOOD_RESOURCE_PRODUCTION = 50;
    private static final int TERRITORY_SIZE = 5;
    private static final long serialVersionUID = 651L;
    private double x;
    private double y;
    private int territoryID;
    private int ownerID;
    private int color;
    private Map<Integer, List<Unit>> unitsMap;

    Territory() {
        this.unitsMap = new HashMap<>();
        setUpMap();
        this.x = 0;
        this.y = 0;
    }

    Territory(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }

    private void setUpMap() {
        for (int i = 0; i <= 6; i++) {
            this.unitsMap.put(i, new ArrayList<>());
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * This method returns the number of units of a specific type on this territory.
     * Note: ALL Units of this type, include both owner's and ally's.
     *
     * @param type type of units.
     * @return the number of units of a specific type on this territory.
     */
    public int getNumOfUnitsForType(int type) {
        return this.unitsMap.get(type).size();
    }

    public int addUnitForType(int type, int number, int ownerID) {
        List<Unit> newUnits = generateUnitForType(type, number, ownerID);
        this.unitsMap.get(type).addAll(newUnits);
        return newUnits.size();
    }

    public static List<Unit> generateUnitForType(int type, int number, int ownerID) {
        List<Unit> newUnits = new ArrayList<>();
        if (type == 0) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new NormalUnit(ownerID));
            }
        } else if (type == 1) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelOne(ownerID));
            }
        } else if (type == 2) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelTwo(ownerID));
            }
        } else if (type == 3) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelThree(ownerID));
            }
        } else if (type == 4) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelFour(ownerID));
            }
        } else if (type == 5) {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelFive(ownerID));
            }
        } else {
            for (int i = 0; i < number; i++) {
                newUnits.add(new UnitLevelSix(ownerID));
            }
        }
        return newUnits;
    }

    public List<Unit> removeUnitForType(int type, int number) throws ExecuteException {
        if (number < 0 || number > this.unitsMap.get(type).size()) {
            throw new ExecuteException("You action caused number of units in territory " + territoryID + " negative\n");
        }
        List<Unit> unitList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            unitList.add(this.unitsMap.get(type).remove(this.unitsMap.get(type).size() - 1));
        }
        return unitList;
    }

    public void removeUnitForType(int type, int number, int ownerID) throws ExecuteException {
        if (number < 0 || number > numOfUnitsOfTypeOfOwner(type, ownerID)) {
            throw new ExecuteException("You action caused number of units in territory " + territoryID + " negative\n");
        }
        List<Unit> unitList = new ArrayList<>();
        for (Unit unit : this.unitsMap.get(type)) {
            if (unitList.size() == number) {
                this.unitsMap.get(type).removeAll(unitList);
                return;
            }
            if (unit.getOwnerId() == ownerID)
                unitList.add(unit);
        }
        this.unitsMap.get(type).removeAll(unitList);
    }

    public List<Unit> removeAllUnitsOfOwner(int ownerID) {
        List<Unit> retreatUnits = new ArrayList<>();
        for (Map.Entry<Integer, List<Unit>> entry : this.unitsMap.entrySet()) {
            List<Unit> allUnitsForThisLevel = entry.getValue();
            for (Unit unit : allUnitsForThisLevel) {
                if (unit.getOwnerId() == ownerID)
                    retreatUnits.add(unit);
            }
        }
        for (Unit unit : retreatUnits) {
            for (Map.Entry<Integer, List<Unit>> entry : this.unitsMap.entrySet()) {
                entry.getValue().remove(unit);
            }
        }
        return retreatUnits;
    }

    public int numOfUnitsOfTypeOfOwner(int type, int ownerID) {
        int num = 0;
        List<Unit> units = this.unitsMap.get(type);
        for (Unit unit : units) {
            if (unit.getOwnerId() == ownerID)
                num = num + 1;
        }
        return num;
    }

    public void setUnitsForType(int type, List<Unit> units) {
        this.unitsMap.put(type, units);
    }


    public void setAttackerUnitsForAllType(List<Unit> units) {
        //re-initialize the units map
        setUpMap();
        for (Unit unit : units) {
            this.unitsMap.get(unit.getType()).add(unit);
        }
    }

    public void addRetreatUnits(List<Unit> retreatUnits) {
        for (Unit unit : retreatUnits) {
            this.unitsMap.get(unit.getType()).add(unit);
        }
    }

    public Set<Integer> getUnitTypes() {
        return unitsMap.keySet();
    }

    public List<Unit> getUnitsForType(int type) {
        return this.unitsMap.get(type);
    }

    public List<Unit> getUnitsForAllType() {
        List<Unit> allUnits = new ArrayList<>();
        for (Map.Entry<Integer, List<Unit>> entry : this.unitsMap.entrySet()) {
            allUnits.addAll(entry.getValue());
        }
        return allUnits;
    }

    public static int getTerritorySize() {
        return TERRITORY_SIZE;
    }

    public int getTechResourceProduction() {
        return TECH_RESOURCE_PRODUCTION;
    }

    public int getFoodResourceProduction() {
        return FOOD_RESOURCE_PRODUCTION;
    }

    public int getTerritoryID() {
        return territoryID;
    }

    public void setTerritoryID(int territoryID) {
        this.territoryID = territoryID;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Territory territory2 = (Territory) super.clone();
        territory2.unitsMap = new HashMap<>();
        for (Map.Entry<Integer, List<Unit>> entry : this.unitsMap.entrySet()) {
            List<Unit> territories2 = new ArrayList<>();
            for (Unit unit : entry.getValue()) {
                territories2.add((Unit) unit.clone());
            }
            territory2.unitsMap.put(entry.getKey(), territories2);
        }
        return territory2;
    }
}