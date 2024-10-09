package nl.rug.oop.rts.graph;

import nl.rug.oop.rts.Observer;
import nl.rug.oop.rts.Subject;
import nl.rug.oop.rts.simulation.Army;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Graph class.
 */
public class Graph implements Subject {
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Observer> observers;

    /**
     * Graph Constructor.
     */
    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Add an observer to the observers list.
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Update all observers in the observers list.
     */
    @Override
    public void updateObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Getter for edges.
     * @return All the edges in the graph.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Getter for nodes.
     * @return List of all the nodes in the graph.
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Add a node to the graph.
     * @param node The node to be added.
     */
    public void addNode(Node node) {
        nodes.add(node);
        updateObservers();
    }

    /**
     * Remove a node from the graph. Also removes all the edges it was connected to.
     * @param node The node to be removed.
     */
    public void removeNode(Node node) {
        for (Edge edge: node.getEdges()) {
            edges.remove(edge);
        }
        nodes.remove(node);
        updateObservers();
    }

    /**
     * Add an edge to the graph.
     * @param edge The edge to be added.
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
        updateObservers();
    }

    /**
     * Create a new edge and add it to the graph.
     * @param node1 The first node the edge should be connected to.
     * @param node2 The second node the edge should be connected to.
     */
    public void createEdge(Node node1, Node node2) {
        // check if edge already exists
        if (!node1.isConnected(node2)) {
            int id = getEdges().size()+1;
            Edge edge = new Edge(id, "edge" + id, node1, node2, observers.get(0));
            addEdge(edge);
        }
    }

    /**
     * Remove an edge from the graph. Also remove it from the edges array of both edges it connected.
     * @param edge The edge to remove.
     */
    public void removeEdge(Edge edge) {
        for (Node node : edge.getNodes()) {
            node.removeEdge(edge);
        }
        edges.remove(edge);
        updateObservers();
    }

    /**
     * Create a node and add it to the graph.
     * @param pos The position at which the node should be.
     */
    public void createNode(Point pos) {
        int amount = nodes.size();
        Node node = new Node(amount, "node" + amount, pos.x, pos.y, observers.get(0));
        addNode(node);
    }

    /**
     * Move a node, then update the observers.
     * @param node The node to be moved.
     * @param pos The position to move the node to.
     */
    public void moveNode(Node node, Point pos) {
        node.setPos(pos);
        updateObservers();
    }

    public void addArmy(Node node, Army army) {
        node.addArmy(army);
        updateObservers();
    }
}
