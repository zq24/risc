package ece651.risc.team11;

import java.util.*;
import java.util.logging.Logger;

/**
 * This class represents the parser that would parse the incoming messages, determine
 * its type and deal with it accordingly.
 */
public class RiscServerMessageParser {
    private final RiscServer server;
    private final Logger logger;
    private final int playerId;

    public RiscServerMessageParser(RiscServer server, Logger logger, int playerId) {
        this.server = server;
        this.logger = logger;
        this.playerId = playerId;
    }

    /**
     * This method determines the message type and deal with it accordingly.
     *
     * @param clientID player's id.
     * @param message  message that was sent from the client.
     */
    public void parseMessage(int clientID, RiscMessage message) {
        RiscMessageType type = message.getType();
        switch (type) {
            case ROOMSIZE:
                int realNumOfPlayers = (int) message.getData();
                server.setNumOfPlayers(realNumOfPlayers);
                logger.info("Room size is set to " + realNumOfPlayers);
                server.broadcastMessage(new RiscMessage(
                        RiscMessageType.READY, server.getPlayerListInRoom()));
                break;
            case GREETING:
                server.broadcastMessage(new RiscMessage(
                        RiscMessageType.READY, server.getPlayerListInRoom()));
                break;
            case READY:
                if (server.setClientReadyAndCheck(clientID, (boolean) message.getData())) {
                    server.setGamePhase(GamePhase.PLAY_TURN_ORDER_VALIDATION_CHECK);
                    logger.info("Game starts");
                    startGame();
                } else {
                    server.broadcastMessage(new RiscMessage(
                            RiscMessageType.READY, server.getPlayerListInRoom()));
                }
                break;
            case ACTION:
                boolean isValid = simulateAndValidate(message);
                if (!isValid) return;

                if (!this.server.setClientReadyAndCheckDuringGame(clientID, true)) {
                    this.server.sendMessageToClient(
                            playerId,
                            new RiscMessage(RiscMessageType.INFO, "Waiting for other players."));
                    return;
                }

                if (!checkAllianceOrderTogether(this.server.getAllAllianceActions())) {
                    this.server.broadcastMessage(new RiscMessage(RiscMessageType.ERROR, "alliance orders do not match"));
                    return;
                }
                executeAllianceOrders(this.server.getAllAllianceActions());
                List<Attack> attacksThatAttackAlliance = attackAlliance(this.server.getAllAttackActions());
                retreatUnits(attacksThatAttackAlliance);
                //Attack is the only action that interfere with each other.
                //So combine them to execute together.
                executeAttackActions(this.server.getAllAttackActions(), this.server.getMap());
                if (checkTurnResult(this.server.getMap())) {
                    return;
                }
                addUnitToAllTerritories(this.server.getMap());
                updateResources(this.server.getMap());
                this.server.setAllAttackActions(new ArrayList<>());
                this.server.setAllAllianceActions(new ArrayList<>());
                this.server.resetReadyState();
                sendMap();
                break;
            case EXITROOM:
                this.server.exitRoom(clientID);
                this.server.broadcastMessage(new RiscMessage(
                        RiscMessageType.READY, this.server.getPlayerListInRoom()));
                break;
            case INFO:
                server.broadcastMessage(new RiscMessage(RiscMessageType.INFO,
                        clientID + ": " + message.getData()));
            default:
                break;
        }
    }

    public List<Attack> attackAlliance(List<Attack> attackList) {
        List<Attack> attackAllianceAction = new ArrayList<>();
        for (Attack attack : attackList) {
            RiscPlayerInRoom attackerInRoom = this.server.getPlayerInRoom(attack.getPlayerID());
            if (!attackerInRoom.hasAlly())
                continue;
            int allyId = attackerInRoom.getPlayerAlliance().get(0);
            int attackDestination = attack.getEndTerritoryID();
            int destinationOwner = this.server.getMap().getTerritoryList().get(attackDestination).getOwnerID();
            if (allyId != destinationOwner)
                continue;
            if (checkDuplicate(attackAllianceAction, attack))
                continue;
            attackAllianceAction.add(attack);
        }
        return attackAllianceAction;
    }

    public boolean checkDuplicate(List<Attack> attackList, Attack newAttack) {
        for (Attack attack : attackList) {
            if (attack.getPlayerID() == newAttack.getPlayerID())
                return true;
        }
        return false;
    }

    public void retreatUnits(List<Attack> attackList) {
        /*
        Player A
        Player B
        They are alliance.
        A have units in B's territories.
        B have units in A's territories.
        A attacks B, B's units that in A's territories should move to nearest B-owned territory.
        In the meantime, A's units that in B's territories should move to nearest A-owned territory.
         */
        for (Attack attack : attackList) {
            int attackDestination = attack.getEndTerritoryID();
            int destinationOwner = this.server.getMap().getTerritoryList().get(attackDestination).getOwnerID();
            List<Territory> AttackerOwnerTerritories = this.server.getMap().getPlayerOwnTerritory(attack.getPlayerID());
            for (Territory territory : AttackerOwnerTerritories) {
                List<Unit> retreatUnits = territory.removeAllUnitsOfOwner(destinationOwner);
                Map<Integer, Set<Integer>> territoryAdjacentList = this.server.getMap().getTerritoryAdjList();
                int nearestTerritoryId = nearestTerritory(territoryAdjacentList, territory.getTerritoryID(), destinationOwner);
                this.server.getMap().getTerritoryList().get(nearestTerritoryId).addRetreatUnits(retreatUnits);
            }

            List<Territory> defenderOwnedTerritories = this.server.getMap().getPlayerOwnTerritory(destinationOwner);
            for (Territory territory : defenderOwnedTerritories) {
                List<Unit> retreatUnits = territory.removeAllUnitsOfOwner(attack.getPlayerID());
                Map<Integer, Set<Integer>> territoryAdjacentList = this.server.getMap().getTerritoryAdjList();
                int nearestTerritoryId = nearestTerritory(territoryAdjacentList, territory.getTerritoryID(), attack.getPlayerID());
                this.server.getMap().getTerritoryList().get(nearestTerritoryId).addRetreatUnits(retreatUnits);
            }

            // break the alliance
            RiscPlayerInRoom attackerInRoom = this.server.getPlayerInRoom(attack.getPlayerID());
            RiscPlayerInRoom allyInRoom = this.server.getPlayerInRoom(destinationOwner);
            attackerInRoom.getPlayerAlliance().clear();
            allyInRoom.getPlayerAlliance().clear();
        }
    }

    public int nearestTerritory(Map<Integer, Set<Integer>> territoryAdjacentList, int startTerritory,
                                int retreatPlayerId) {

        Map<Integer, Integer> map = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(startTerritory);
        map.put(startTerritory, 1);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (this.server.getMap().getTerritoryList().get(current).getOwnerID() == retreatPlayerId) {
                return current;
            }
            Set<Integer> neighbors = territoryAdjacentList.get(current);
            int dist = map.get(current);

            for (int neigh : neighbors) {
                if (!map.containsKey(neigh) && territoryAdjacentList.containsKey(neigh)) {
                    map.put(neigh, dist + 1);
                    queue.add(neigh);
                }
            }
        }
        return -1;
    }

    /**
     * This method checks if all the alliance orders are valid at the same time.
     *
     * @param allianceList all the alliance orders in this turn.
     * @return true if all the alliance orders are valid at the same time. Otherwise return false.
     */
    public boolean checkAllianceOrderTogether(List<Alliance> allianceList) {
        // only allow one ally and they have to match with each other.
        Map<Integer, Integer> allianceMatch = new HashMap<>();
        for (Alliance alliance : allianceList) {
            if (!allianceMatch.containsKey(alliance.getPlayerID())) {
                allianceMatch.put(alliance.getPlayerID(), alliance.getAllianceId());
            } else {
                return false;
            }
        }
        for (Map.Entry<Integer, Integer> entry : allianceMatch.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if (!allianceMatch.containsKey(value))
                return false;
            if (allianceMatch.get(value) != key)
                return false;
        }
        return true;
    }

    /**
     * This method executes all the alliance orders.
     *
     * @param allianceList all the alliance orders.
     */
    public void executeAllianceOrders(List<Alliance> allianceList) {
        for (Alliance alliance : allianceList) {
            RiscPlayerInRoom playerInRoom = this.server.getPlayerInRoom(alliance.getPlayerID());
            playerInRoom.getPlayerAlliance().add(alliance.getAllianceId());
        }
    }

    /**
     * clone a map and try to simulate the whole process of execution.
     * As long as there is invalid action, it will catch an exception
     * If all of actions are valid, execute them on real map.
     *
     * @param message information that was sent from the client.
     */
    public boolean simulateAndValidate(RiscMessage message) {
        List<Attack> attacks = new ArrayList<>();
        List<Alliance> alliances = new ArrayList<>();
        List<Action> actions = convertInstructionToAction(
                (List<Instruction>) message.getData(),
                this.server.getMap(), attacks, alliances);
        //clone a map to simulate the process, if invalid, throw exception
        String errorInfo = validateAllActions(actions, true);
        if (errorInfo != null) {
            server.sendMessageToClient(
                    playerId,
                    new RiscMessage(RiscMessageType.ERROR, errorInfo));
            return false;
        }
        //If no problem, execute them on real map
        validateAllActions(actions, false);
        this.server.addAttackActions(attacks);
        this.server.addAllianceAction(alliances);
        return true;
    }

    public void sendPlayerInfo() {
        for (int key : server.getIDSet()) {
            server.sendMessageToClient(key,
                    new RiscMessage(RiscMessageType.PLAYER, server.getPlayerInRoom(key)));
        }
    }

    /**
     * This method would execute the attack orders.
     *
     * @param attackActions the attack orders.
     * @param riscMap       Map for the game.
     */
    public void executeAttackActions(List<Attack> attackActions, RiscMap riscMap) {
        System.out.println("Execute Attack Action; There are " + attackActions.size() + " attack actions");
        Map<Integer, Map<Integer, List<Unit>>> playersAttackMap = getPlayersAttackMap(attackActions);
        /*for (Map.Entry<Integer, Map<Integer, List<Unit>>> entry: playersAttackMap.entrySet()) {
            System.out.println("There are " + entry.getValue().size() + " unique player attacks territory " + entry.getKey());
        }*/
        //System.out.println();

        Map<Integer, Map<RiscPlayerInRoom, List<Unit>>> playsAllianceAttackMap = combineAllianceAttack(playersAttackMap);
        /*for (Map.Entry<Integer, Map<RiscPlayerInRoom, List<Unit>>> entry: playsAllianceAttackMap.entrySet()) {
            System.out.println("There are " + entry.getValue().size() + " unique alliance attacks territory " + entry.getKey());
        }
        System.out.println();*/

        for (Map.Entry<Integer, Map<RiscPlayerInRoom, List<Unit>>> entry : playsAllianceAttackMap.entrySet()) {
            int destination = entry.getKey();
            Map<RiscPlayerInRoom, List<Unit>> allPlayerThatAttackThisTerritory = entry.getValue();
            randomExecuteAttack(allPlayerThatAttackThisTerritory, destination, riscMap);
        }
    }

    /**
     * This method randomly execute the attack orders if these orders' destination territory is the same.
     *
     * @param allAttackerAttackSameTerritory contains all attacking units of all players who attack the same territory.
     * @param destination                    attacker destination territory.
     * @param riscMap                        Map for the game.
     */
    public void randomExecuteAttack(Map<RiscPlayerInRoom, List<Unit>> allAttackerAttackSameTerritory, int destination, RiscMap riscMap) {
        System.out.println("Number of attack to this particular territory in Map: " + allAttackerAttackSameTerritory.size());
        List<Map.Entry<RiscPlayerInRoom, List<Unit>>> allAttackersInEntry = new ArrayList<>(allAttackerAttackSameTerritory.entrySet());
        System.out.println("Number of attack to this particular territory: " + allAttackersInEntry.size());
        Random random = new Random();
        while (allAttackersInEntry.size() != 0) {
            int randomIndex = random.nextInt(allAttackersInEntry.size());
            Map.Entry<RiscPlayerInRoom, List<Unit>> randomAttack = allAttackersInEntry.get(randomIndex);
            rollDiceFight(randomAttack, destination, riscMap);
            allAttackersInEntry.remove(randomIndex);
            System.out.println("Remaining number of attack: " + allAttackersInEntry.size());
        }
    }

    /**
     * This method resolves the combat result.
     *
     * @param attackMap   attacking units of one player.
     * @param destination attacker's destination territory.
     * @param riscMap     Map for the game.
     */
    public void rollDiceFight(Map.Entry<RiscPlayerInRoom, List<Unit>> attackMap, int destination, RiscMap riscMap) {

        List<Unit> defenceList = riscMap.getTerritoryList().get(destination).getUnitsForAllType();
        List<Unit> attackList = attackMap.getValue();
        System.out.println("defenceList: " + defenceList.size());
        System.out.println("attackList: " + attackList.size());
        sortUnitsOrderByBonus(defenceList);
        sortUnitsOrderByBonus(attackList);
        Dice dice = new Dice(20);
        boolean attackHigh = true;
        while (defenceList.size() != 0 && attackList.size() != 0) {
            int defence = dice.rollDice();
            int attack = dice.rollDice();
            //System.out.println("defence point: " + defence);
            //System.out.println("attack point: " + attack);
            if (attackHigh) {
                attack += attackList.get(attackList.size() - 1).getBonus();
                defence += defenceList.get(0).getBonus();
                if (defence >= attack) {
                    attackList.remove(attackList.size() - 1);
                    //System.out.println("1 Attacker lost");
                } else {

                    try {
                        riscMap.getTerritoryList().get(destination).removeUnitForType(defenceList.get(0).getType(), 1);
                    } catch (ExecuteException ex) {
                        //Won't happen. No need to handle
                        ex.printStackTrace();
                    }
                    defenceList.remove(0);
                    //System.out.println("1 Defender lost");
                }
                attackHigh = false;
            } else {
                attack += attackList.get(0).getBonus();
                defence += defenceList.get(defenceList.size() - 1).getBonus();
                if (defence >= attack) {
                    attackList.remove(0);
                    //System.out.println("1 Attacker lost");
                } else {
                    try {
                        riscMap.getTerritoryList().get(destination).removeUnitForType(defenceList.get(0).getType(), 1);
                    } catch (ExecuteException ex) {
                        //Won't happen. No need to handle
                        ex.printStackTrace();
                    }
                    defenceList.remove(defenceList.size() - 1);
                    //System.out.println("1 Defender lost");
                }
                attackHigh = true;
            }
        }
        if (defenceList.size() == 0) {
            riscMap.getTerritoryList().get(destination).setOwnerID(decideOwner(attackList));
            riscMap.getTerritoryList().get(destination).setAttackerUnitsForAllType(attackList);
            //System.out.println("Defender lost the fight");
        } else {
            System.out.println("Attacker lost the fight");
            //System.out.println("End: unit number in defender territory" + riscMap.getTerritoryList().get(destination).getUnitsForAllType().size());
        }
        //System.out.println("End: defenceList: " + defenceList.size());
        //System.out.println("End: attackList: " + attackList.size());

    }

    /**
     * This method determines the owner of a territory after an attack.
     *
     * @param attackList the remaining attack units.
     * @return the owner of a territory.
     */
    public int decideOwner(List<Unit> attackList) {
        Map<Integer, Integer> remainForce = new HashMap<>();
        for (Unit unit : attackList) {
            int ownerId = unit.getOwnerId();
            if (!remainForce.containsKey(ownerId)) {
                remainForce.put(ownerId, 1);
            } else {
                remainForce.put(ownerId, remainForce.get(ownerId) + 1);
            }
        }
        List<Map.Entry<Integer, Integer>> allRemainForce = new ArrayList<>(remainForce.entrySet());
        allRemainForce.sort((t0, t1) -> t1.getValue() - t0.getValue());
        return allRemainForce.get(0).getKey();
    }

    /**
     * This method sorts the units based on its bonus.
     *
     * @param units units.
     */
    public void sortUnitsOrderByBonus(List<Unit> units) {
        units.sort(Comparator.comparingInt(Unit::getBonus));
    }

    private Map<Integer, Map<RiscPlayerInRoom, List<Unit>>> combineAllianceAttack(Map<Integer, Map<Integer, List<Unit>>> attackMap) {
        //System.out.println("Inside combineAllianceAttack function");
        Map<Integer, Map<RiscPlayerInRoom, List<Unit>>> playersAllianceAttackMap = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, List<Unit>>> mapEntry : attackMap.entrySet()) {
            int territoryId = mapEntry.getKey();
            //System.out.println("Territory Id: " + territoryId);
            Map<Integer, List<Unit>> allPlayersAttackThisTerritory = mapEntry.getValue();
            List<RiscPlayerInRoom> allPlayersInRoom = new ArrayList<>();
            for (Map.Entry<Integer, List<Unit>> entry : allPlayersAttackThisTerritory.entrySet()) {
                int playerId = entry.getKey();
                allPlayersInRoom.add(this.server.getPlayerInRoom(playerId));
            }
            //System.out.println("Number of unique player in room: " + allPlayersInRoom.size());
            for (int i = 0; i < allPlayersInRoom.size(); i++) {
                RiscPlayerInRoom playerInRoom = allPlayersInRoom.get(i);
                if (!playerInRoom.hasAlly()) {
                    playersAllianceAttackMap.putIfAbsent(territoryId, new HashMap<>());
                    Map<RiscPlayerInRoom, List<Unit>> attackForce = playersAllianceAttackMap.get(territoryId);
                    attackForce.put(playerInRoom, allPlayersAttackThisTerritory.get(playerInRoom.getPlayerID()));
                    continue;
                }
                //System.out.println("has ally");
                int allianceId = playerInRoom.getPlayerAlliance().get(0);
                RiscPlayerInRoom allianceInRoom = this.server.getPlayerInRoom(allianceId);
                if (!allPlayersInRoom.contains(allianceInRoom)) {
                    playersAllianceAttackMap.putIfAbsent(territoryId, new HashMap<>());
                    Map<RiscPlayerInRoom, List<Unit>> attackForce = playersAllianceAttackMap.get(territoryId);
                    attackForce.put(playerInRoom, allPlayersAttackThisTerritory.get(playerInRoom.getPlayerID()));
                    continue;
                }
                //System.out.println("has ally and attack the same territory");
                playersAllianceAttackMap.putIfAbsent(territoryId, new HashMap<>());
                Map<RiscPlayerInRoom, List<Unit>> attackForce = playersAllianceAttackMap.get(territoryId);
                List<Unit> combinedForce = new ArrayList<>();
                combinedForce.addAll(allPlayersAttackThisTerritory.get(playerInRoom.getPlayerID()));
                combinedForce.addAll(allPlayersAttackThisTerritory.get(allianceId));
                attackForce.put(playerInRoom, combinedForce);
                allPlayersInRoom.remove(allianceInRoom);
            }
        }
        return playersAllianceAttackMap;
    }

    /**
     * This method get all attacking units of all players and corresponding destination.
     *
     * @param attackActions attack actions of all players.
     * @return attacking units of all players and corresponding destination.
     */
    public Map<Integer, Map<Integer, List<Unit>>> getPlayersAttackMap(List<Attack> attackActions) {
        Map<Integer, Map<Integer, List<Unit>>> playersAttackMap = new HashMap<>();
        for (Attack attack : attackActions) {
            List<Unit> attackers = Territory.generateUnitForType(attack.getUnitType(), attack.getNumOfUnits(), attack.getPlayerID());
            if (!playersAttackMap.containsKey(attack.getEndTerritoryID())) {
                playersAttackMap.put(attack.getEndTerritoryID(), new HashMap<>());
                int playerId = attack.getPlayerID();
                Map<Integer, List<Unit>> curPlayerAttackMap = playersAttackMap.get(attack.getEndTerritoryID());
                curPlayerAttackMap.put(playerId, attackers);
            } else {
                Map<Integer, List<Unit>> curPlayerAttackMap = playersAttackMap.get(attack.getEndTerritoryID());
                int playerId = attack.getPlayerID();
                if (!curPlayerAttackMap.containsKey(playerId)) {
                    curPlayerAttackMap.put(playerId, attackers);
                } else {
                    curPlayerAttackMap.get(playerId).addAll(attackers);
                }
            }
        }
        return playersAttackMap;
    }

    //set number of territories for each player and number of units for each territory
    public void startGame() {
        server.resetReadyState();
        server.initializeMap();
        sendMap();
    }

    private void sendMap() {
        server.broadcastMessage(new RiscMessage(RiscMessageType.INFO, "\nPlease enter new actions"));
        server.broadcastMessage(new RiscMessage(RiscMessageType.ACTION, server.getMap()));
        for (int key : server.getIDSet()) {
            server.sendMessageToClient(key,
                    new RiscMessage(RiscMessageType.PLAYER, server.getPlayerInRoom(key)));
        }
    }

    /**
     * This method updates the number of resources a player has according the territories
     * this player owns.
     *
     * @param riscMap game map.
     */
    public void updateResources(RiscMap riscMap) {
        int updateTechAmount = this.server.getUpdateTechAmount();
        int updateFoodAmount = this.server.getUpdateFoodAmount();
        for (Territory territory : riscMap.getTerritoryList()) {
            List<Technology> updateTechResources = new ArrayList<>(updateTechAmount);
            List<Food> updateFoodResources = new ArrayList<>(updateFoodAmount);
            int ownerID = territory.getOwnerID();
            RiscPlayerInRoom owner = server.getPlayerInRoom(ownerID);
            owner.addTechResource(updateTechResources);
            owner.addFoodResource(updateFoodResources);
        }
    }

    /**
     * This method converts all the Instruction objects to Action objects; meanwhile, it checks
     * if each of actions is valid or not.
     *
     * @param instructions all instructions sent from a client.
     * @param riscMap      Map for the game.
     * @return a list of Action if every single of Action object is valid; otherwise return null.
     */
    public List<Action> convertInstructionToAction(List<Instruction> instructions, RiscMap riscMap,
                                                   List<Attack> attacks, List<Alliance> alliances) {
        List<Action> actions = new ArrayList<>();
        for (Instruction instruction : instructions) {
            if (instruction.getActionType().equals("m") || instruction.getActionType().equals("move")) {
                Move move = new Move(this.playerId,
                        Integer.parseInt(instruction.getNumOfUnits()),
                        Integer.parseInt(instruction.getSource()),
                        Integer.parseInt(instruction.getDestination()),
                        Integer.parseInt(instruction.getStartUnitType()));
                if (!move.validateAction(riscMap, this.server))
                    return null;
                actions.add(move);
            }
            if (instruction.getActionType().equals("a") || instruction.getActionType().equals("attack")) {
                Attack attack = new Attack(this.playerId,
                        Integer.parseInt(instruction.getNumOfUnits()),
                        Integer.parseInt(instruction.getSource()),
                        Integer.parseInt(instruction.getDestination()),
                        Integer.parseInt(instruction.getStartUnitType()));
                if (!attack.validateAction(riscMap, this.server))
                    return null;
                actions.add(attack);
                attacks.add(attack);
            }
            if (instruction.getActionType().equals("u") || instruction.getActionType().equals("upgrade")) {
                Upgrade upgrade = new Upgrade(this.playerId,
                        Integer.parseInt(instruction.getNumOfUnits()),
                        Integer.parseInt(instruction.getStartUnitType()),
                        Integer.parseInt(instruction.getEndUnitType()),
                        Integer.parseInt(instruction.getSource()));
                if (!upgrade.validateAction(riscMap, this.server))
                    return null;
                actions.add(upgrade);
            }
            if (instruction.getActionType().equals("alliance")) {
                Alliance alliance = new Alliance(this.playerId, 0, Integer.parseInt(instruction.getAllianceId()));
                if (!alliance.validateAction(riscMap, this.server))
                    return null;
                actions.add(alliance);
                alliances.add(alliance);
            }
        }
        for (Action action : actions) {
            if (action instanceof Attack) {
                System.out.println(action.toString());
            } else if (action instanceof Move) {
                System.out.println(action.toString());
            } else {
                System.out.println(action.toString());
            }
        }
        return actions;
    }

    /**
     * This method add one new basic unit in each territory.
     *
     * @param riscMap Map for the game.
     */
    public void addUnitToAllTerritories(RiscMap riscMap) {
        for (Territory territory : riscMap.getTerritoryList()) {
            // add one basic unit
            territory.addUnitForType(0, 1, territory.getOwnerID());
            RiscPlayerInRoom player = server.getPlayerInRoom(territory.getOwnerID());
            player.addNumFoodResource(territory.getFoodResourceProduction());
            player.addNumTechResource(territory.getTechResourceProduction());
        }
    }

    /**
     * Check the result at the end of each turn.
     *
     * @param riscMap Map for the game.
     * @return true if a player wins the game; otherwise return false.
     */
    public boolean checkTurnResult(RiscMap riscMap) {
        Set<Integer> activeSet = new HashSet<>();
        for (int i = 0; i < riscMap.getTerritoryList().size(); i++) {
            Territory territory = riscMap.getTerritoryList().get(i);
            activeSet.add(territory.getOwnerID());
            territory.setColor(server.getMap().getColor(territory.getOwnerID()));
        }
        if (activeSet.size() == 1) {
            server.broadcastMessage(
                    new RiscMessage(RiscMessageType.WIN, activeSet.iterator().next()));
            server.restartGame();
            return true;
        }
        for (int checkActivePlayerID : server.getIDSet()) {
            if (!activeSet.contains(checkActivePlayerID)) {
                if (server.getRoom(0).isActive(checkActivePlayerID)) {
                    server.sendMessageToClient(
                            checkActivePlayerID, new RiscMessage(RiscMessageType.LOST, null));
                    server.getRoom(0).setActive(checkActivePlayerID, false);
                }
            }
        }
        return false;
    }

    /**
     * This method is for checking all the input actions at a time; used only after done
     * checking for every single action. If it isSimulation is true, it will copy the corresponding player to
     * do the simulation. Otherwise, use the real player
     * If invalid, return error information. If valid, return null;
     *
     * @param actions      List of actions for this player at this turn.
     * @param isSimulation True if it execute on fakeMap fakePlayer; false if execute on real map & player
     * @return null if there is no error message
     */
    public String validateAllActions(List<Action> actions, boolean isSimulation) {
        if (actions == null) {
            return "Invalid Single Action";
        }
        if (actions.size() == 0) {
            return null;
        }
        Action actionSingle = actions.get(0);
        RiscPlayerInRoom realPlayer = this.server.getPlayerInRoom(actionSingle.getPlayerID());
        RiscPlayerInRoom fakePlayer = realPlayer;
        RiscMap fakeMap = this.server.getMap();
        if (isSimulation) {
            try {
                fakePlayer = (RiscPlayerInRoom) realPlayer.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            try {
                fakeMap = (RiscMap) this.server.getMap().clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        for (Action action : actions) {
            try {
                action.executeAction(fakeMap, fakePlayer);
            } catch (ExecuteException ex) {
                return ex.getMessage();
            }
        }
        return null;
    }
}