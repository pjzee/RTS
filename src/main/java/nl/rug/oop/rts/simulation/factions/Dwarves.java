package nl.rug.oop.rts.simulation.factions;

import java.util.ArrayList;

/**
 * Dwarves class.
 * Extends Faction.
 */
public class Dwarves extends Faction {
    /**
     * Constructor for Dwarves.
     */
    public Dwarves() {
        unitNames = new ArrayList<>();
        unitNames.add("Guardian");
        unitNames.add("Phalanx");
        unitNames.add("Axe Thrower");
        teamNum = 0;
        factionName = "Dwarves";
    }
}
