package nl.rug.oop.rts.graph;

import nl.rug.oop.rts.Observer;
import nl.rug.oop.rts.Subject;
import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.events.Event;

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

/**
 * Node class.
 */
public class Node implements ArmyLocation, Subject {
    private final int id;
    private String name;
    private List<Edge> edges;
    private final int size = 80;
    private Point pos;
    private List<Army> armiesPresent = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    /**
     * Node constructor.
     *
     * @param name The name of the node.
     * @param id The node's id.
     * @param x The x coordinate of the node.
     * @param y The y coordinate of the node.
     * @param observer An observer that needs to be notified whenever this node updates.
     */
    public Node(int id, String name, int x, int y, Observer observer) {
        this.id = id;
        this.name = name;
        this.edges = new ArrayList<Edge>();
        this.pos = new Point(x, y);
        addObserver(observer);
    }

    /**
     * Add an army to this node.
     * @param army The army to be added.
     */
    public void addArmy(Army army) {
        armiesPresent.add(army);
        updateObservers();
    }

    public List<Army> getArmiesPresent() {
        return armiesPresent;
    }

    @Override
    public List<ArmyLocation> getAdjacentLocations() {
        return new ArrayList<>(getEdges());
    }

    /**
     * Remove an army from this node.
     * @param army The army to be removed.
     */
    public void removeArmy(Army army) {
        armiesPresent.remove(army);
        updateObservers();
    }

    /**
     * Getter for the node's id.
     *
     * @return The node's id;
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the node's name.
     *
     * @return The node's name;
     */
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    /**
     * Getter for the node's list of edges.
     *
     * @return The node's list of edges;
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Adds an edge to the node.
     *
     * @param edge The edge to add.
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    /**
     * Remove an edge from the edge list.
     * @param edge The edge to be removed.
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
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

    /**
     * Getter for size.
     * @return Size.
     */
    public int getSize(){
        return size;
    }

    /**
     * Setter for pos.
     * @param newPos The new position of the node.
     */
    public void setPos(Point newPos) {
        pos.x = newPos.x;
        pos.y = newPos.y;
    }

    /**
     * Check if this node is connected to another node.
     * @param other The other node.
     * @return Whether there is an edge connecting this node to the other node.
     */
    public boolean isConnected(Node other) {
        for (Edge edge: edges) {
            if (edge.getNodes().contains(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Setter for pos.
     * @return The position of the node.
     */
    public Point getPos() {
        return pos;
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

    /**
     * Returns a list of all nodes that this node is connected to.
     * @return The list.
     */
    public List<Node> getAdjacentNodes() {
        List<Node> result = new ArrayList<>();
        for (Edge edge: edges) {
            Node otherNode;
            otherNode = edge.getNodes().get(0);
            if (otherNode == this) {
                otherNode = edge.getNodes().get(1);
            }
            result.add(otherNode);
        }
        return result;
    }

    /**
     * Get the edge that connects this node to a given node.
     * @param node The other node.
     * @return null if there is no edge between this and node, else the edge that connects them.
     */
    public Edge getEdgeTo(Node node) {
        for (Edge edge: edges) {
            if (edge.getNodes().contains(node)) {
                return edge;
            }
        }
        return null;
    }
}