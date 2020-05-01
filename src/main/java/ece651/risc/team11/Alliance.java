package ece651.risc.team11;

public class Alliance extends Action {

    private int allianceId;

    public Alliance(int allianceId) {
        this.allianceId = allianceId;
    }

    public Alliance(int playerID, int numOfUnits, int allianceId) {
        super(playerID, numOfUnits);
        this.allianceId = allianceId;
    }

    @Override
    public boolean validateAction(RiscMap riscMap, RiscServer riscServer) {

        if (this.allianceId == this.playerID)
            return false;

        RiscPlayerInRoom playerInRoom = riscServer.getPlayerInRoom(this.playerID);
        return !playerInRoom.hasAlly();
    }

    @Override
    public void executeAction(RiscMap riscMap, RiscPlayerInRoom playerInRoom) {
    }

    public int getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(int allianceId) {
        this.allianceId = allianceId;
    }
}