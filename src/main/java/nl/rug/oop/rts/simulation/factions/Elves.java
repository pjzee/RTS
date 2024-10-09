package nl.rug.oop.rts.simulation.factions;

import java.util.ArrayList;

/**
 * Dwarves class.
 * Extends Faction.
 */
public class Elves extends Faction {
    /**
     * Constructor for Elves.
     */
    public Elves() {
        unitNames = new ArrayList<>();
        unitNames.add("Lorien Warrior");
        unitNames.add("Mirkwood Archer");
        unitNames.add("Rivendell Lancer");
        teamNum = 0;
        factionName = "Elves";
    }
}
