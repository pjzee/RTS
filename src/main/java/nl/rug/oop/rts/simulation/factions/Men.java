package nl.rug.oop.rts.simulation.factions;

import java.util.ArrayList;

/**
 * Dwarves class.
 * Extends Faction.
 */
public class Men extends Faction {
    /**
     * Constructor for Men.
     */
    public Men() {
        unitNames = new ArrayList<>();
        unitNames.add("Gondor Soldier");
        unitNames.add("Tower Guard");
        unitNames.add("Ithilien Ranger");
        teamNum = 0;
        factionName = "Men";
    }
}
