package ece651.risc.team11;

import java.util.*;

/**
 * This class represents the move order.
 */
public class Move extends Action {

    private int startTerritoryID;

    private int endTerritoryID;

    private int unitType;

    private int numOfFoodResourcesNeeded;

    public Move() {
        super();
    }

    public Move(int playerID, int numOfUnits, int startTerritoryID, int endTerritoryID, int unitType) {
        super(playerID, numOfUnits);
        this.startTerritoryID = startTerritoryID;
        this.endTerritoryID = endTerritoryID;
        this.unitType = unitType;
    }


    @Override
    public boolean validateAction(RiscMap riscMap, RiscServer server) {
        RiscPlayerInRoom playerInRoom = server.getPlayerInRoom(playerID);
        Map<Integer, Set<Integer>> playersTerritoryAdjList =
                riscMap.getPlayersTerritoryAdjList(playerID, server);
        if (!playersTerritoryAdjList.containsKey(startTerritoryID) ||
                !playersTerritoryAdjList.containsKey(endTerritoryID)) {
            return false;
        }
        HashMap<Integer, Boolean> visited = new HashMap<>();
        for (int key : playersTerritoryAdjList.keySet()) {
            visited.put(key, false);
        }
        depthFirstSearch(startTerritoryID, visited, playersTerritoryAdjList);

        if (!visited.get(endTerritoryID))
            return false;

        Map<Integer, Set<Integer>> playersTerritoryAdjacentList =
                riscMap.getPlayersTerritoryAdjList(playerID, server);

        int shortestPath = minimumCostPathLength(playersTerritoryAdjacentList);
        this.numOfFoodResourcesNeeded = (Territory.getTerritorySize() * shortestPath) * this.numOfUnits;
        return this.numOfFoodResourcesNeeded <= playerInRoom.numOfFoodResource();
    }

    @Override
    public void executeAction(RiscMap riscMap, RiscPlayerInRoom playerInRoom) throws ExecuteException {
        Territory sourceTerritory = riscMap.getTerritoryList().get(this.startTerritoryID);
        Territory destinationTerritory = riscMap.getTerritoryList().get(this.endTerritoryID);
        sourceTerritory.removeUnitForType(this.unitType, this.numOfUnits, this.playerID);
        destinationTerritory.addUnitForType(this.unitType, this.numOfUnits, this.playerID);
        playerInRoom.removeFoodResource(this.numOfFoodResourcesNeeded);
    }

    /**
     * This method checks if there is a path from input source to input destination.
     *
     * @param v       source territory id.
     * @param visited keep track of visited territory.
     * @param adjList a player's adjacency lists.
     */
    private void depthFirstSearch(int v,
                                  Map<Integer, Boolean> visited,
                                  Map<Integer, Set<Integer>> adjList) {
        visited.replace(v, true);
        for (int territoryID : adjList.get(v)) {
            if (visited.containsKey(territoryID)) {
                if (!visited.get(territoryID)) {
                    depthFirstSearch(territoryID, visited, adjList);
                }
            }
        }
    }

    /**
     * This method determines the path from source to destination with minimum cost.
     *
     * @param playersTerritoryAdjacentList a player's adjacency lists.
     * @return minimum path distance.
     */
    private int minimumCostPathLength(Map<Integer, Set<Integer>> playersTerritoryAdjacentList) {

        // use a map to record the distance for each territory from the starting territory
        Map<Integer, Integer> map = new HashMap<>();

        // BFS traversal
        // initialize the queue
        Queue<Integer> queue = new LinkedList<>();

        // add the starting point to the queue
        queue.add(this.startTerritoryID);
        map.put(this.startTerritoryID, 1);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == this.endTerritoryID) {
                return map.get(current);
            }
            // get all the neighbors of this territory
            Set<Integer> neighbors = playersTerritoryAdjacentList.get(current);
            int dist = map.get(current);

            for (int neigh : neighbors) {
                if (!map.containsKey(neigh) && playersTerritoryAdjacentList.containsKey(neigh)) {
                    map.put(neigh, dist + 1);
                    queue.add(neigh);
                }
            }
        }
        return 0;
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
        return "Move{" +
                "startTerritoryID=" + startTerritoryID +
                ", endTerritoryID=" + endTerritoryID +
                ", unitType=" + unitType +
                ", numOfFoodResourcesNeeded=" + numOfFoodResourcesNeeded +
                ", playerID=" + playerID +
                ", numOfUnits=" + numOfUnits +
                '}';
    }
}