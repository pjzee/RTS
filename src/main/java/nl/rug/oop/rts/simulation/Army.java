package nl.rug.oop.rts.simulation;

import nl.rug.oop.rts.graph.ArmyLocation;
import nl.rug.oop.rts.graph.Node;
import nl.rug.oop.rts.simulation.events.Event;
import nl.rug.oop.rts.simulation.factions.Faction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Army class. Can be deployed to a node.
 * Has a certain faction.
 */
public class Army {
    private Faction faction;
    private ArmyLocation location;
    private List<Unit> units = new ArrayList<>();
    private Node onWayTo;

    /**
     * Constructor for army.
     * @param unitsNum The number of units.
     * @param faction The faction this army belongs to.
     * @param location The location at which this army resides.
     */
    public Army(int unitsNum, Faction faction, ArmyLocation location) {
        this.faction = faction;
        this.location = location;
        addUnits(unitsNum);

    }

    /**
     * Add some units to this army.
     * @param amount The amount of units to be added.
     */
    public void addUnits(int amount) {
        for (int i = 0; i < amount; i++) {
            Random random = new Random();
            String name = faction.getUnitNames().get(random.nextInt(faction.getUnitNames().size()));
            units.add(new Unit(name, random.nextInt(15) + 20, random.nextInt(15) + 20, this));
        }
    }

    public Faction getFaction() {
        return faction;
    }

    /**
     * Move this army to a new location.
     * Remove it from its current location and add it to the new location.
     * @param newLocation The new location.
     */
    public void moveArmy(ArmyLocation newLocation) {
        location.removeArmy(this);
        newLocation.addArmy(this);
        location = newLocation;
        resolveEvents();
    }

    public ArmyLocation getLocation() {
        return location;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public int getUnitsNum() {
        return units.size();
    }

    /**
     * Remove a number of units from this army.
     * Simply removes from the back of the array.
     * If there are no more units after this, there is no more army, so the army will be removed from its location.
     * @param amount The amount of units to remove.
     */
    public void removeUnits(int amount) {
        int newNum = units.size() - amount;
        if (newNum <= 0) {
            units.clear();
            location.removeArmy(this);
            return;
        }
        List<Unit> toRemove = new ArrayList<>();
        for (int i = newNum; i < units.size(); i++) {
            toRemove.add(units.get(i));
        }
        units.removeAll(toRemove);
    }

    /**
     * Remove a single unit from this army. If there are no more units left after this, there is no more army,
     * so the army needs to be removed from its location.
     * @param unit The unit to be removed.
     */
    public void removeUnit(Unit unit) {
        units.remove(unit);
        if (units.isEmpty()) {
            this.location.removeArmy(this);
        }
    }

    /**
     * Check if there are events present on the current node. If there are, 50% chance for the event to happen.
     */
    public void resolveEvents() {
        List<Event> events = location.getEvents();
        if (events.isEmpty()) {
            return;
        }
        Random randomizer = new Random();
        if (randomizer.nextInt(100) < 50) {
            // event happens
            Event event = events.get(randomizer.nextInt(events.size()));
            event.carryOutEvent(this);
        }
    }

    public Node getOnWayTo() {
        return onWayTo;
    }

    public void setOnWayTo(Node onWayTo) {
        this.onWayTo = onWayTo;
    }
}
