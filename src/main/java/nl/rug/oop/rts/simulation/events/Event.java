package nl.rug.oop.rts.simulation.events;

import nl.rug.oop.rts.simulation.Army;

import javax.swing.*;

/**
 * Abstract event class. An event can be on an army location. Each event has a different effect. When an army
 * moves to a location with an event, there is a chance the event triggers.
 */
public abstract class Event {
    /**
     * The name of this event.
     */
    protected String name;

    /**
     * The explanation of what this event does.
     */
    protected String explanation;

    /**
     * Create a popup sharing with the user the event that happened and to which army.
     * @param army The army the event happened to.
     */
    public void createPopUp(Army army) {
        JOptionPane.showMessageDialog(null, "The following event happened to the army " +
                "of faction " + army.getFaction().getFactionName() + ":\n" + explanation);
    }

    /**
     * Process the event.
     * @param army The army that the event is happening to.
     */
    public abstract void processEvent(Army army);

    /**
     * Carry out the event. First call the processEvent method which changes the state of the graph, then create a
     * popup telling the user what happened.
     * @param army The army the event is happening to.
     */
    public void carryOutEvent(Army army) {
        processEvent(army);
        createPopUp(army);
    }

    /**
     * Create a new event.
     * @param name The name of the event.
     * @return The new event.
     */
    public static Event createEvent(String name) {
        return switch (name) {
            case "Fog event" -> new FogEvent();
            case "Change of mind event" -> new ChangeOfMindEvent();
            default -> new RebellionEvent();
        };
    }

    public String getName() {
        return name;
    }

}
