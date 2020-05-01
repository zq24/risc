package ece651.risc.team11;

import java.util.Map;
import java.util.Set;

/**
 * This class represents attack orders.
 */
public class Attack extends Action {

    private int startTerritoryID;

    private int endTerritoryID;

    private int unitType;

    private int numOfFoodResourcesNeeded;

    public Attack() {
        super();
    }

    public Attack(int playerID, int numOfUnits, int startTerritoryID, int endTerritoryID, int unitType) {
        super(playerID, numOfUnits);
        this.startTerritoryID = startTerritoryID;
        this.endTerritoryID = endTerritoryID;
        this.unitType = unitType;
        this.numOfFoodResourcesNeeded = this.numOfUnits;
    }

    public Attack(Action action, int startTerritoryID, int endTerritoryID, int unitType) {
        super(action);
        this.startTerritoryID = startTerritoryID;
        this.endTerritoryID = endTerritoryID;
        this.unitType = unitType;
        this.numOfFoodResourcesNeeded = this.numOfUnits;
    }

    /**
     * This method checks if this attack order is valid or not.
     *
     * @param riscMap Map for the game.
     * @param server  game server.
     * @return true if this attack order is valid; otherwise return false.
     */
    @Override
    public boolean validateAction(RiscMap riscMap, RiscServer server) {
        RiscPlayerInRoom playerInRoom = server.getPlayerInRoom(playerID);
        int destinationOwner = server.getMap().getTerritoryList().get(this.endTerritoryID).getOwnerID();
        int allyId;
        Map<Integer, Set<Integer>> playersTerritoryAdjList;
        if (playerInRoom.hasAlly()) {
            allyId = playerInRoom.getPlayerAlliance().get(0);
            if (allyId == destinationOwner) {
                playersTerritoryAdjList = riscMap.getPlayersTerritoryAdjList(playerID);
            } else {
                playersTerritoryAdjList = riscMap.getPlayersTerritoryAdjList(playerID, server);
            }
        } else {
            playersTerritoryAdjList = riscMap.getPlayersTerritoryAdjList(playerID);
        }
        System.out.println(playersTerritoryAdjList);
        if (!playersTerritoryAdjList.containsKey(startTerritoryID) ||
                playersTerritoryAdjList.containsKey(endTerritoryID)) {
            return false;
        }

        return playersTerritoryAdjList.get(startTerritoryID).contains(endTerritoryID) &&
                (this.numOfFoodResourcesNeeded <= playerInRoom.numOfFoodResource());
    }

    //execution of attack is simply moving units
    @Override
    public void executeAction(RiscMap riscMap, RiscPlayerInRoom playerInRoom) throws ExecuteException {
        Territory sourceTerritory = riscMap.getTerritoryList().get(this.startTerritoryID);

        sourceTerritory.removeUnitForType(this.unitType, this.numOfUnits, this.getPlayerID());
        playerInRoom.removeFoodResource(this.numOfFoodResourcesNeeded);

    }

    public int getStartTerritoryID() {
        return startTerritoryID;
    }

    public int getEndTerritoryID() {
        return endTerritoryID;
    }

    public int getUnitType() {
        return unitType;
    }

    public int getNumOfFoodResourcesNeeded() {
        return numOfFoodResourcesNeeded;
    }

    @Override
    public String toString() {
        return "Attack{" +
                "startTerritoryID=" + startTerritoryID +
                ", endTerritoryID=" + endTerritoryID +
                ", unitType=" + unitType +
                ", numOfFoodResourcesNeeded=" + numOfFoodResourcesNeeded +
                ", playerID=" + playerID +
                ", numOfUnits=" + numOfUnits +
                '}';
    }
}