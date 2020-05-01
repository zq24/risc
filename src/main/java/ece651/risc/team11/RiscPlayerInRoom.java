package ece651.risc.team11;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RiscPlayerInRoom implements Serializable, Cloneable {

    private static final long serialVersionUID = 651L;
    private static final int INITIAL_NUM_FOOD = 300;
    private static final int INITIAL_NUM_TECH = 300;

    private boolean isReady;
    private boolean isActive;
    private int playerID;
    private int colorID;

    private List<Food> foodResource;
    private List<Technology> technologyResource;

    private List<Integer> playerAlliance;

    public RiscPlayerInRoom() {
        this.isActive = true;
        this.foodResource = new ArrayList<>();
        this.technologyResource = new ArrayList<>();
        this.playerAlliance = new ArrayList<>();
        resetAllResource();
    }

    public RiscPlayerInRoom(RiscPlayer player) {
        this();
        this.playerID = player.getPlayerID();
    }

    public void resetAllResource() {
        this.technologyResource.clear();
        this.foodResource.clear();
        this.playerAlliance.clear();
        addNumFoodResource(INITIAL_NUM_FOOD);
        addNumTechResource(INITIAL_NUM_TECH);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public void setPlayerID(int id) {
        this.playerID = id;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public List<Food> getFoodResource() {
        return foodResource;
    }

    /*public void setFoodResource(List<Food> foodResource) {
        this.foodResource = foodResource;
    }*/

    public List<Technology> getTechnologyResource() {
        return technologyResource;
    }

    /*public void setTechnologyResource(List<Technology> technologyResource) {
        this.technologyResource = technologyResource;
    }*/

    public void addFoodResource(List<Food> newFoodResource) {
        this.foodResource.addAll(newFoodResource);
    }

    public void addTechResource(List<Technology> newTechResource) {
        this.technologyResource.addAll(newTechResource);
    }

    public void addNumFoodResource(int numOfFood) {
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < numOfFood; i++) {
            foods.add(new Food());
        }
        addFoodResource(foods);
    }

    public void addNumTechResource(int numOfTech) {
        List<Technology> technologies = new ArrayList<>();
        for (int i = 0; i < numOfTech; i++) {
            technologies.add(new Technology());
        }
        addTechResource(technologies);
    }

    public void removeFoodResource(int numOfFoodResource) throws ExecuteException {

        if (numOfFoodResource > 0) {
            if (numOfFoodResource > this.foodResource.size()) {
                throw new ExecuteException("Your food resource is not enough!");
            }
            this.foodResource.subList(0, numOfFoodResource).clear();
        }


    }

    public void removeTechResource(int numOfTechResource) throws ExecuteException {
        if (numOfTechResource > 0) {
            if (numOfTechResource > this.technologyResource.size()) {
                throw new ExecuteException("Your technology resource is not enough!");
            }
            this.technologyResource.subList(0, numOfTechResource).clear();
        }
    }

    public int numOfFoodResource() {
        return this.foodResource.size();
    }

    public int numOfTechResource() {
        return this.technologyResource.size();
    }

    public List<Integer> getPlayerAlliance() {
        return playerAlliance;
    }

    public void setPlayerAlliance(List<Integer> playerAlliance) {
        this.playerAlliance = playerAlliance;
    }

    public boolean hasAlly() {
        return this.playerAlliance.size() != 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RiscPlayerInRoom player = (RiscPlayerInRoom) super.clone();
        player.technologyResource = new ArrayList<>();
        player.foodResource = new ArrayList<>();
        for (Food food : this.foodResource) {
            player.foodResource.add((Food) food.clone());
        }
        for (Technology technology : this.technologyResource) {
            player.technologyResource.add((Technology) technology.clone());
        }
        return player;
    }
}