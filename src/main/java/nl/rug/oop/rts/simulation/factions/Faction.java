package nl.rug.oop.rts.simulation.factions;

import java.util.List;
import java.util.Random;

/**
 * Abstract class representing a faction. Each faction has a couple of unit names to choose from and belongs to a team,
 * 0 or 1. Each faction also has a name.
 */
public abstract class Faction {
    /**
     * List of names that a unit belonging to this faction can have.
     */
    protected List<String> unitNames;
    /**
     * Which team this belongs to, 1 or 0.
     */
    protected int teamNum;
    /**
     * The name of this faction.
     */
    protected String factionName;

    /**
     * Create a random faction belonging to the specified team.
     * @param teamNum The number of the team the faction should belong to.
     * @return The newly created faction.
     */
    public static Faction createRandomFaction(int teamNum) {
        Random random = new Random();
        if (teamNum == 0) {
            int randInt = random.nextInt(3);
            return switch (randInt) {
                case 0 -> new Men();
                case 1 -> new Dwarves();
                default -> new Elves(); // default instead of case 2 because it makes the squiggly lines go away :)
            };
        } else {
            int randInt = random.nextInt(2);
            if (randInt == 0) {
                return new Isengard();
            }
            return new Mordor();
        }
    }

    /**
     * Getter for teamName.
     * @return The name of the team this faction belongs to.
     */
    public int getTeamNum() {
        return teamNum;
    }

    public String getFactionName() {
        return factionName;
    }

    public List<String> getUnitNames() {
        return unitNames;
    }
}
