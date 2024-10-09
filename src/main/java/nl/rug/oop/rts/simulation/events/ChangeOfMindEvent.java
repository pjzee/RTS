package nl.rug.oop.rts.simulation.events;

import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.factions.Faction;

/**
 * Event that causes the army to split into two with one half joining the other team.
 * Extends event.
 */
public class ChangeOfMindEvent extends Event {
    /**
     * Constructor for this event.
     */
    public ChangeOfMindEvent() {
        name = "Change of mind event";
        explanation = "Half of the units in this army change their minds and form a new army, "+
                "fighting for the other team.";
    }

    /**
     * Process the event. Split the army in two, with the second half becoming an army belonging to a random faction
     * of the other team.
     * @param army The army that the event is happening to.
     */
    @Override
    public void processEvent(Army army) {
        int unitsNum = army.getUnitsNum();
        int newNum = unitsNum / 2;
        army.removeUnits(newNum);
        int newTeam = Math.abs(army.getFaction().getTeamNum() - 1);
        Faction faction = Faction.createRandomFaction(newTeam);
        Army newArmy = new Army(newNum, faction, army.getLocation());
        army.getLocation().addArmy(newArmy);
    }
}
