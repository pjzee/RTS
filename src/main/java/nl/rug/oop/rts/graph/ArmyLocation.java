package nl.rug.oop.rts.graph;

import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.events.Event;

import java.util.List;

/**
 * Interface for locations that are able to hold an army.
 * Implemented by node and edge.
 */
public interface ArmyLocation {
    void addArmy(Army army);

    void removeArmy(Army army);

    void addEvent(Event event);

    void removeEvent(Event event);

    List<Event> getEvents();

    List<Army> getArmiesPresent();

    List<ArmyLocation> getAdjacentLocations();
}
