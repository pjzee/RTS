package nl.rug.oop.rts.simulation.factions;

import java.util.ArrayList;

/**
 * Isengard class.
 * Extends Faction.
 */
public class Isengard extends Faction {

    /**
     * Constructor for Isengard.
     */
    public Isengard() {
        unitNames = new ArrayList<>();
        unitNames.add("Uruk-hai");
        unitNames.add("Uruk Crossbowman");
        unitNames.add("Warg Rider");
        teamNum = 1;
        factionName = "Isengard";
    }
}
