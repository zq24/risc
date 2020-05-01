package ece651.risc.team11;

import java.util.ArrayList;
import java.util.Arrays;

public class Upgrade extends Action {

    private int startUnitType;

    private int endUnitType;

    private int upgradeTerritoryID;

    private int numOfTechResourceNeeded;

    private static final ArrayList<Integer> COSTS = new ArrayList<>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));

    public Upgrade() {
        super();
    }

    public Upgrade(int playerID, int numOfUnits, int startUnitType, int endUnitType, int upgradeTerritoryID) {
        super(playerID, numOfUnits);
        this.startUnitType = startUnitType;
        this.endUnitType = endUnitType;
        this.upgradeTerritoryID = upgradeTerritoryID;
    }

    public Upgrade(Action action, int startUnitType, int endUnitType, int upgradeTerritoryID) {
        super(action);
        this.startUnitType = startUnitType;
        this.endUnitType = endUnitType;
        this.upgradeTerritoryID = upgradeTerritoryID;
    }

    @Override
    public boolean validateAction(RiscMap riscMap, RiscServer server) {
        RiscPlayerInRoom playerInRoom = server.getPlayerInRoom(playerID);
        if (this.upgradeTerritoryID >= riscMap.getTerritoryList().size())
            return false;
        // check if you own that territory first
        if (riscMap.getTerritoryList().get(this.upgradeTerritoryID).getOwnerID() != this.playerID)
            return false;
        System.out.println("The territory belongs to you");
        if (this.startUnitType < 0 || this.startUnitType > 6 || this.endUnitType < 0 || this.endUnitType > 6) {
            return false;
        }
        System.out.println("The entered type is correct");
        if (this.startUnitType >= this.endUnitType) {
            return false;
        }
        System.out.println("The started type is smaller than the ended type");
        int totalCost = calculateCost();
        this.numOfTechResourceNeeded = totalCost;
        return costOutOfBoundary(totalCost, playerInRoom);
    }

    @Override
    public void executeAction(RiscMap riscMap, RiscPlayerInRoom playerInRoom) throws ExecuteException {
        Territory targetTerritory = riscMap.getTerritoryList().get(this.upgradeTerritoryID);
        targetTerritory.removeUnitForType(this.startUnitType, this.numOfUnits, this.playerID);
        targetTerritory.addUnitForType(this.endUnitType, this.numOfUnits, this.playerID);
        playerInRoom.removeTechResource(this.numOfTechResourceNeeded);
    }

    public boolean costOutOfBoundary(int totalCost, RiscPlayerInRoom playerInRoom) {
        int numOfTech = playerInRoom.numOfTechResource();
        return totalCost <= numOfTech;
    }

    public int calculateCost() {
        return this.numOfUnits * (COSTS.get(this.endUnitType) - COSTS.get(this.startUnitType));
    }

    public int getStartUnitType() {
        return startUnitType;
    }

    public int getEndUnitType() {
        return endUnitType;
    }

    public int getUpgradeTerritoryID() {
        return upgradeTerritoryID;
    }

    public int getNumOfTechResourceNeeded() {
        return numOfTechResourceNeeded;
    }

    @Override
    public String toString() {
        return "Upgrade{" +
                "startUnitType=" + startUnitType +
                ", endUnitType=" + endUnitType +
                ", upgradeTerritoryID=" + upgradeTerritoryID +
                ", numOfTechResourceNeeded=" + numOfTechResourceNeeded +
                ", playerID=" + playerID +
                ", numOfUnits=" + numOfUnits +
                '}';
    }
}