package ece651.risc.team11;

import java.io.Serializable;

/**
 * This class shows the format of how orders would be saved.
 */
public class Instruction implements Serializable {
    private static final long serialVersionUID = -7532593836296710725L;

    /**
     * This field records the action type of the order (move or attack)
     */
    private String actionType;

    /**
     * This field records the source territory.
     */
    private String source;

    /**
     * This field records the destination territory.
     */
    private String destination;

    /**
     * This field records how many units that get involved in this order.
     */
    private String numOfUnits;

    private String startUnitType;

    private String endUnitType;

    private String allianceId;

    public Instruction() {
    }

    public Instruction(String actionType, String source, String destination, String numOfUnits, String startUnitType, String endUnitType, String allianceId) {
        this.actionType = actionType;
        this.source = source;
        this.destination = destination;
        this.numOfUnits = numOfUnits;
        this.startUnitType = startUnitType;
        this.endUnitType = endUnitType;
        this.allianceId = allianceId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getNumOfUnits() {
        return numOfUnits;
    }

    public String getStartUnitType() {
        return startUnitType;
    }

    public String getEndUnitType() {
        return endUnitType;
    }

    public String getAllianceId() {
        return allianceId;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "actionType='" + actionType + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", numOfUnits='" + numOfUnits + '\'' +
                '}';
    }
}