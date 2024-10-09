package nl.rug.oop.rts.simulation.events;

import nl.rug.oop.rts.simulation.Army;

/**
 * Event that causes some new units to be added to an army.
 * Extends Event.
 */
public class RebellionEvent extends Event {
    /**
     * Constructor for this event.
     */
    public RebellionEvent() {
        name = "Rebellion event";
        explanation = "The finds some angry citizens who are willing to help it fight its enemy. It gains 2 units.";
    }

    /**
     * Add 2 units to the army.
     * @param army The army that the event is happening to.
     */
    @Override
    public void processEvent(Army army) {
        int amountAdded = 2;
        army.addUnits(2);
    }
}
