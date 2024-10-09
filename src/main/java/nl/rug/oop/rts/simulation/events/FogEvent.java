package nl.rug.oop.rts.simulation.events;

import nl.rug.oop.rts.simulation.Army;

/**
 * Extends Event class. This event removes some units from the army.
 * (They lose each other in the fog).
 */
public class FogEvent extends Event {
    /**
     * Constructor for FogEvent.
     */
    public FogEvent() {
        name = "Fog event";
        explanation = """
                The army gets lost in the fog and some units gets lost!\
                The army decides looking for them is not worth it.
                This army loses 20% of their units.""";
    }

    /**
     * Remove 20% of units from the army.
     * @param army The army that the event is happening to.
     */
    @Override
    public void processEvent(Army army) {
        int amountRemoved = (int) (army.getUnitsNum() * 0.2);
        army.removeUnits(amountRemoved);
    }
}
