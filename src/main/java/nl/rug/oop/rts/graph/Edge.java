package nl.rug.oop.rts.graph;

import nl.rug.oop.rts.Observer;
import nl.rug.oop.rts.Subject;
import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.events.Event;

import java.util.List;
import java.util.ArrayList;

/**
 * Edge class. Connects two nodes.
 */
public class Edge implements ArmyLocation, Subject {
    private final int id;
    private String name;
    private final List<Node> nodes;
    private List<Army> armiesPresent = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    /**
     * Edge constructor.
     *
     * @param id    The edge's id.
     * @param name  The edge's name.
     * @param node1 The first node the edge connects to.
     * @param node2 The second node the edge connects to.
     * @param observer An observer that needs to be notified when this edge updates.
     */

    public Edge(int id, String name, Node node1, Node node2, Observer observer) {
        this.id = id;
        this.name = name;
        this.nodes = new ArrayList<Node>();
        node1.addEdge(this);
        node2.addEdge(this);
        nodes.add(node1);
        nodes.add(node2);
        addObserver(observer);
    }

    /**
     * Getter for the edge's id.
     *
     * @return The edge's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Add an army to this node.
     * @param army The army to be added.
     */
    public void addArmy(Army army) {
        armiesPresent.add(army);
        updateObservers();
    }

    /**
     * Remove an army from this node.
     * @param army The army to be removed.
     */
    public void removeArmy(Army army) {
        armiesPresent.remove(army);
        updateObservers();
    }

    public List<Army> getArmiesPresent() {
        return armiesPresent;
    }

    @Override
    public List<ArmyLocation> getAdjacentLocations() {
        return new ArrayList<>(getNodes());
    }

    /**
     * Getter for the edge's name.
     *
     * @return The edge's name.
     */
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    /**
     * Getter for the edge's connected nodes.
     *
     * @return The edge's connected nodes.
     */
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public void addEvent(Event event) {
        events.add(event);
        updateObservers();
    }

    @Override
    public void removeEvent(Event event) {
        events.remove(event);
        updateObservers();
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void updateObservers() {
        for (Observer observer: observers) {
            observer.update();
        }
    }
}
