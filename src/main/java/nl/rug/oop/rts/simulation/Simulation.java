package nl.rug.oop.rts.simulation;

import nl.rug.oop.rts.graph.ArmyLocation;
import nl.rug.oop.rts.graph.Edge;
import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulation class. Handles the simulation steps. Changes the state of armies.
 */
public class Simulation {
    private final Graph graph;
    private int currentStepPhase = 0;
    private final int stepsAmount = 5;
    private final Random randomizer = new Random();

    public Simulation(Graph graph) {
        this.graph = graph;
    }

    /**
     * Increment the step phase and make sure it loops back after the last step.
     */
    private void incrementStepPhase() {
        currentStepPhase++;
        currentStepPhase = currentStepPhase % stepsAmount;
    }

    /**
     * Handles the step of the simulation that moves units from a node to an edge.
     */
    private void moveToEdge() {
        List<Army> armies = new ArrayList<>();
        for (Node node: graph.getNodes()) {
            armies.addAll(node.getArmiesPresent());
        }
        for (Army army: armies) {
            Node currentLocation = (Node)army.getLocation();
            List<Node> adjacentNodes = currentLocation.getAdjacentNodes();
            Node destNode = adjacentNodes.get(randomizer.nextInt(adjacentNodes.size()));
            army.setOnWayTo(destNode);
            army.moveArmy(currentLocation.getEdgeTo(destNode));
        }
    }

    /**
     * Handles the step of the simulation that moves units from an edge to a node.
     */
    private void moveToNode() {
        List<Army> armies = new ArrayList<>();
        for (Edge edge: graph.getEdges()) {
            armies.addAll(edge.getArmiesPresent());
        }
        for (Army army: armies) {
            army.moveArmy(army.getOnWayTo());
            army.setOnWayTo(null);
        }
    }

    /**
     * Simulate one step of the simulation.
     * Then increment the step phase.
     */
    public void simulateStep() {
        if (currentStepPhase % 2 == 0) {
            boolean battleHappened = resolveBattles();
            if (!battleHappened) {
                // if no battle happened, simulate next step as well.
                incrementStepPhase();
                simulateStep();
            }
        } else if (currentStepPhase == 1) {
            moveToEdge();
        } else {
            moveToNode();
        }
        incrementStepPhase();
    }

    /**
     * Get all the locations in the graph.
     * @return A list of all the locations in the graph.
     */
    private List<ArmyLocation> getLocations() {
        List<ArmyLocation> locations = new ArrayList<>();
        locations.addAll(graph.getNodes());
        locations.addAll(graph.getEdges());
        return locations;
    }

    /**
     * Check if at least one army from both teams are present at a location.
     * @param location The location to check.
     * @return A boolean that is true if both teams are present, false if not.
     */
    private boolean bothTeamsPresent(ArmyLocation location) {
        List<Army> armies = location.getArmiesPresent();
        if (armies.size() >= 2) {
            int teamNum = armies.get(0).getFaction().getTeamNum();
            for (int i = 1; i < armies.size(); i++) {
                if (teamNum != armies.get(i).getFaction().getTeamNum()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resolve all the battles. Check which locations have both teams present and call the battle function there.
     * @return If a battle occurred or not.
     */
    private boolean resolveBattles() {
        boolean battleHappened = false;
        for (ArmyLocation location: getLocations()) {
            if (bothTeamsPresent(location)) {
                battle(location);
                battleHappened = true;
            }
        }
        return battleHappened;
    }

    /**
     * Resolve a battle at a location.
     * Add all units from one team to one list and all from the other to another.
     * Calculate the ratio of the amount of units.
     * Make first unit from one team fight first unit from the others, etc.
     * The team with more units get some bonus damage based on the ratio of units. (The idea being that the extra units
     * would help out since they are not fighting).
     * Continue fighting until one team has no more units.
     * @param location The location of the battle.
     */
    private void battle(ArmyLocation location) {
        List<Army> armies;
        List<Unit> units1 = new ArrayList<>();
        List<Unit> units2 = new ArrayList<>();

        do {
            armies = location.getArmiesPresent();
            units1.clear();
            units2.clear();

            for (Army army : armies) {
                if (army.getFaction().getTeamNum() == 0) {
                    units1.addAll(army.getUnits());
                } else {
                    units2.addAll(army.getUnits());
                }
            }
            double armyRatio = (double) units1.size() / (double) units2.size();
            boolean team1Advantage = true;
            if (armyRatio < 1) {
                team1Advantage = false;
                armyRatio = 1 / armyRatio;
            }

            Unit unit1, unit2;
            for (int i = 0; i < Math.min(units1.size(), units2.size()); i++) {
                unit1 = units1.get(i);
                unit2 = units2.get(i);
                if (team1Advantage) {
                    unit2.takeDamage((int) (unit1.getDamage() * armyRatio));
                    unit1.takeDamage(unit2.getDamage());
                } else {
                    unit1.takeDamage((int) (unit2.getDamage() * armyRatio));
                    unit2.takeDamage(unit1.getDamage());
                }
            }
        } while (bothTeamsPresent(location));
    }
}
