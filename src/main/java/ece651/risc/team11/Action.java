package ece651.risc.team11;

/**
 * This class represents the generic order that a player may issue.
 */
public abstract class Action {

    /**
     * This field represents the player who issued this action.
     */
    protected int playerID;

    /**
     * This field represents the number of units that get involved in this action.
     */
    protected int numOfUnits;

    public Action() {
    }

    public Action(int playerID, int numOfUnits) {
        this.playerID = playerID;
        this.numOfUnits = numOfUnits;
    }

    public Action(Action action) {
        this.playerID = action.playerID;
        this.numOfUnits = action.numOfUnits;
    }

    /**
     * This method would check if this action is valid or not; override in child class.
     *
     * @param riscMap Map for the game.
     * @return true if this action is valid; otherwise return false.
     */
    public abstract boolean validateAction(RiscMap riscMap, RiscServer riscServer);

    public abstract void executeAction(RiscMap riscMap, RiscPlayerInRoom playerInRoom) throws ExecuteException;

    public int getPlayerID() {
        return playerID;
    }

    public int getNumOfUnits() {
        return numOfUnits;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setNumOfUnits(int numOfUnits) {
        this.numOfUnits = numOfUnits;
    }
}