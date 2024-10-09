package nl.rug.oop.rts.simulation.factions;

import java.util.ArrayList;

/**
 * Mordor class.
 * Extends Faction.
 */
public class Mordor extends Faction {
    /**
     * Constructor for Mordor.
     */
    public Mordor() {
        unitNames = new ArrayList<>();
        unitNames.add("Orc Warrior");
        unitNames.add("Orc Pikeman");
        unitNames.add("Haradrim Archer");
        teamNum = 1;
        factionName = "Mordor";
    }
}
