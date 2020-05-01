package ece651.risc.team11;

import java.io.Serializable;
import java.util.*;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the map used in a RISC game.
 */
public class RiscMap implements Serializable, Cloneable {
    private static final long serialVersionUID = 651L;

    public int getNumberOfTerritories() {
        return numberOfTerritories;
    }

    private final int numberOfTerritories;
    private List<Territory> territoryList;
    private final Map<Integer, Set<Integer>> territoryAdjList;
    private final Random rand = new Random();
    private final int size = 1000;
    private final List<Integer> playerIDList;

    public RiscMap(int numberOfTerritoriesPerGroup,
                   int numberOfGroup,
                   List<Integer> playerIDList) {
        this.numberOfTerritories = numberOfTerritoriesPerGroup * numberOfGroup;
        this.playerIDList = playerIDList;
        territoryAdjList = new HashMap<>();
        for (int i = 0; i < numberOfTerritories; i++) {
            territoryAdjList.put(i, new HashSet<>());
        }

        do {
            territoryList = createRandomVerticesList();
            easyDelaunayTriangulation();
        } while (!checkIsValidGraph());
        splitTerritoriesInGroup(numberOfTerritoriesPerGroup, numberOfGroup);
    }

    public List<Territory> getPlayerOwnTerritory(int playerId) {
        List<Territory> playerOwnTerritories = new ArrayList<>();
        for (Territory territory : this.getTerritoryList()) {
            if (territory.getOwnerID() == playerId)
                playerOwnTerritories.add(territory);
        }
        return playerOwnTerritories;
    }

    public int getColor(int playerID) {
        for (int i = 0; i < playerIDList.size(); i++) {
            if (playerIDList.get(i) == playerID) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Split the map into groups for different players.
     *
     * @param numOfGroup - number of group
     */
    private void splitTerritoriesInGroup(int numberOfTerritoriesPerGroup, int numOfGroup) {
        boolean[] visited = new boolean[numberOfTerritories];
        for (int i = 0; i < numOfGroup - 1; i++) {
            int root = findFarthestPoint(visited);
            territoryList.get(root).setColor(i);
            territoryList.get(root).setOwnerID(playerIDList.get(i));
            breathFirstSearch(root, numberOfTerritoriesPerGroup, visited);
        }

        for (int i = 0; i < numberOfTerritories; i++) {
            if (!visited[i]) {
                visited[i] = true;
                territoryList.get(i).setColor(numOfGroup - 1);
                territoryList.get(i).setOwnerID(playerIDList.get(numOfGroup - 1));
            }
        }
    }

    private int findFarthestPoint(boolean[] visited) {
        int farthest = 0;
        for (int i = 0; i < numberOfTerritories; i++) {
            if (!visited[i]) {
                farthest = i;
                break;
            }
        }
        double minDist =
                Math.pow(territoryList.get(farthest).getX() - size / 2, 2)
                        + Math.pow(territoryList.get(farthest).getY() - size / 2, 2);
        for (int i = 0; i < numberOfTerritories; i++) {
            if (!visited[i]) {
                Territory territory = territoryList.get(i);
                double dist = Math.pow(territory.getX() - size / 2, 2) + Math.pow(territory.getY() - size / 2, 2);
                if (dist > minDist) {
                    farthest = i;
                    minDist = dist;
                }
            }
        }
        return farthest;
    }

    private void breathFirstSearch(int root, int number, boolean[] visited) {
        Comparator<Integer> comparator = new Comparator<>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return territoryAdjList.get(integer).size() - territoryAdjList.get(t1).size();
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(numberOfTerritories, comparator);
        priorityQueue.add(root);

        while (priorityQueue.size() != 0) {
            int n = priorityQueue.poll();
            if (!visited[n]) {
                visited[n] = true;
                territoryList.get(n).setOwnerID(territoryList.get(root).getOwnerID());
                territoryList.get(n).setColor(territoryList.get(root).getColor());
                number--;
                for (int territoryID : territoryAdjList.get(n)) {
                    if (!visited[territoryID]) {
                        priorityQueue.add(territoryID);
                    }
                }
            }
            if (number == 0) {
                return;
            }
        }
    }

    /**
     * Get the adj list of the territories owned by a specific player.
     *
     * @param playerID id of the player.
     * @param server   game server.
     * @return the adj list of territories.
     */
    public Map<Integer, Set<Integer>> getPlayersTerritoryAdjList(int playerID, RiscServer server) {
        Map<Integer, Set<Integer>> playersTerritoryAdjList = new HashMap<>();
        for (int key : territoryAdjList.keySet()) {
            if (territoryList.get(key).getOwnerID() == playerID ||
                    server.getPlayerInRoom(territoryList.get(key).getOwnerID()).getPlayerAlliance().contains(playerID)) {
                playersTerritoryAdjList.put(key, new HashSet<>(territoryAdjList.get(key)));
            }
        }
        return playersTerritoryAdjList;
    }

    public Map<Integer, Set<Integer>> getPlayersTerritoryAdjList(int playerID) {
        Map<Integer, Set<Integer>> playersTerritoryAdjList = new HashMap<>();
        for (int key : territoryAdjList.keySet()) {
            if (territoryList.get(key).getOwnerID() == playerID) {
                playersTerritoryAdjList.put(key, new HashSet<>(territoryAdjList.get(key)));
            }
        }
        return playersTerritoryAdjList;
    }

    /**
     * Getter for adjacency list of the map
     *
     * @return territoryAdjList
     */
    public Map<Integer, Set<Integer>> getTerritoryAdjList() {
        return territoryAdjList;
    }

    public List<Territory> getTerritoryList() {
        return territoryList;
    }

    /**
     * Create a list of random points on the map
     *
     * @return list of random points
     */
    private List<Territory> createRandomVerticesList() {
        List<Territory> point2DArrayList = new ArrayList<>();
        for (int i = 0; i < numberOfTerritories; i++) {
            Territory territory = new NormalTerritory(rand.nextInt(size), rand.nextInt(size));
            point2DArrayList.add(territory);
            territory.setTerritoryID(i);
        }
        return point2DArrayList;
    }

    /**
     * Check whether the graph is connected
     *
     * @return true if the graph is connected otherwise false
     */
    private boolean checkIsValidGraph() {
        if (numberOfTerritories < 2) {
            return true;
        }
        boolean[] visited = new boolean[numberOfTerritories];
        for (int i = 0; i < numberOfTerritories; i++) {
            visited[i] = false;
        }
        depthFirstSearch(0, visited);

        for (int i = 0; i < numberOfTerritories; i++) {
            if (!visited[i]) {
                return false;
            }
        }
        return true;
    }

    private void depthFirstSearch(int v, boolean[] visited) {
        visited[v] = true;
        for (int territoryID : territoryAdjList.get(v)) {
            if (!visited[territoryID]) {
                depthFirstSearch(territoryID, visited);
            }
        }
    }

    /**
     * Perform Delaunay Triangulation on the point set. It uses a O(n^4) algorithm for simplicity,
     * but this should not be a big problem since the number of territory should not be large
     */
    private void easyDelaunayTriangulation() {
        if (numberOfTerritories == 2) {
            territoryAdjList.get(0).add(1);
            territoryAdjList.get(1).add(0);
        }

        Rectangle rectangle = new Rectangle(0, 0, size, size);
        for (int i = 0; i < numberOfTerritories; i++) {
            for (int j = i + 1; j < numberOfTerritories; j++) {
                for (int k = j + 1; k < numberOfTerritories; k++) {
                    boolean isValidTriangle = true;
                    Circle circle = circumcircleOfTriangle(
                            new Point2D(territoryList.get(i).getX(), territoryList.get(i).getY()),
                            new Point2D(territoryList.get(j).getX(), territoryList.get(j).getY()),
                            new Point2D(territoryList.get(k).getX(), territoryList.get(k).getY()));
                    if (!rectangle.contains(circle.getCenterX(), circle.getCenterY())) {
                        isValidTriangle = false;
                    } else {
                        // Check if there is any vertex inside the circumcircle of the triangle.
                        for (int a = 0; a < numberOfTerritories; a++) {
                            if (a == i || a == j || a == k) continue;
                            if (circle.contains(
                                    territoryList.get(a).getX(),
                                    territoryList.get(a).getY())) {
                                isValidTriangle = false;
                                break;
                            }
                        }
                    }
                    if (isValidTriangle) {
                        territoryAdjList.get(i).add(j);
                        territoryAdjList.get(j).add(i);
                        territoryAdjList.get(i).add(k);
                        territoryAdjList.get(k).add(i);
                        territoryAdjList.get(j).add(k);
                        territoryAdjList.get(k).add(j);
                    }
                }
            }
        }
    }

    /**
     * Create a circumcircle with three 2D points
     *
     * @param p1 - 2D points 1
     * @param p2 - 2D points 2
     * @param p3 - 2D points 3
     * @return the circumcircle
     */
    private Circle circumcircleOfTriangle(Point2D p1, Point2D p2, Point2D p3) {
        double slopeA = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
        double slopeB = (p3.getY() - p2.getY()) / (p3.getX() - p2.getX());

        double centerX =
                (slopeA * slopeB * (p1.getY() - p3.getY())
                        + slopeB * (p1.getX() + p2.getX())
                        - slopeA * (p2.getX() + p3.getX())
                ) / (2 * (slopeB - slopeA));
        double centerY =
                -1 * (centerX - (p1.getX() + p2.getX()) / 2) / slopeA
                        + (p1.getY() + p2.getY()) / 2;

        double r = Math.sqrt(Math.pow(centerX - p1.getX(), 2) + Math.pow(centerY - p1.getY(), 2));
        return new Circle(centerX, centerY, r);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RiscMap map2 = (RiscMap) super.clone();
        map2.territoryList = new ArrayList<>();
        for (Territory territory : this.territoryList) {
            map2.territoryList.add((Territory) territory.clone());
        }
        return map2;
    }
}