package nl.rug.oop.rts.swing;

import nl.rug.oop.rts.Observer;
import nl.rug.oop.rts.graph.ArmyLocation;
import nl.rug.oop.rts.graph.Edge;
import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.graph.Node;
import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.events.Event;
import nl.rug.oop.rts.util.TextureLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main panel class. Used for drawing the graph and controlling the environment.
 */
public class MainPanel extends JPanel implements Observer {
    private final Graph graph;
    private final OptionsMenu optionsMenu;
    /**
     * The node that is currently selected. There can only be one node selected at a time. May be NULL.
     */
    private Node selectedNode;
    /**
     * The edge that is currently selected. There can only be one edge selected at a time. May be NULL.
     */
    private Edge selectedEdge;
    private boolean waiting = false;
    private final JButton deleteNode;
    private final JButton deleteEdge;
    private final JButton addEdge;
    private final JButton addArmy;
    private final JButton addEvent;

    /**
     * Constructor for panel.
     * @param graph The graph that should be drawn.
     * @param buttons The list of buttons that should be added to the menu bar.
     * @param optionsMenu The other pane, the options menu.
     */
    public MainPanel(Graph graph, ArrayList<JButton> buttons, OptionsMenu optionsMenu) {
        this.setBackground(new Color(215, 196, 196));
        this.setBounds(0, 0, 600, 600);

        this.graph = graph;
        this.optionsMenu = optionsMenu;

        Mouse handler = new Mouse(graph, this);
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);

        this.deleteNode = buttons.get(1);
        this.deleteEdge = buttons.get(3);
        this.addEdge = buttons.get(2);
        this.addArmy = buttons.get(4);
        this.addEvent = buttons.get(6);
    }

    /**
     * Set the selected node.
     * @param node The node that will become the selected node.
     */
    public void setSelectedNode(Node node) {
        selectedNode = node;
        deleteNode.setEnabled(true);
        addEdge.setEnabled(true);
        addArmy.setEnabled(true);
        addEvent.setEnabled(true);
        optionsMenu.selectNode(node);
        update();
    }

    /**
     * Clear the selected node variable, update the correct buttons.
     */
    public void clearSelectedNode() {
        selectedNode = null;
        deleteNode.setEnabled(false);
        addEdge.setEnabled(false);
        addArmy.setEnabled(false);
        addEvent.setEnabled(false);
        optionsMenu.deselectAll();
        update();
    }

    /**
     * Setter for waiting field.
     * @param n The new value of waiting.
     */
    public void setWaiting(boolean n) {
        waiting = n;
    }

    /**
     * Getter for waiting field.
     * @return Whether or not the program is waiting for a second click to create an edge.
     */
    public boolean getWaiting() {
        return waiting;
    }

    /**
     * Setter for selected edge. Update buttons and options menu.
     * @param edge The new selectedEdge.
     */
    public void setSelectedEdge(Edge edge) {
        selectedEdge = edge;
        deleteEdge.setEnabled(true);
        addArmy.setEnabled(true);
        addEvent.setEnabled(true);
        optionsMenu.selectEdge(edge);
        update();
    }

    /**
     * Clear the selected edge. Update buttons and options menu.
     */
    public void clearSelectedEdge() {
        selectedEdge = null;
        deleteEdge.setEnabled(false);
        addArmy.setEnabled(false);
        addEvent.setEnabled(false);
        optionsMenu.deselectAll();
        update();
    }

    public Edge getSelectedEdge() {
        return selectedEdge;
    }

    /**
     * Get the selected location. Can be both an edge and a node.
     * @return The selected location.
     */
    public ArmyLocation getSelectedLocation() {
        if (selectedEdge != null) {
            return selectedEdge;
        }
        if (selectedNode != null) {
            return selectedNode;
        }
        return null;
    }

    /**
     * Paint the panel. Overriding from Jpanel.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEdges(g);
        drawNodes(g);
        drawEvents(g);
    }

    private Point calculateCenter(Edge edge) {
        List<Node> nodes = edge.getNodes();
        return new Point((nodes.get(0).getPos().x + nodes.get(1).getPos().x) / 2,
                (nodes.get(0).getPos().y + nodes.get(1).getPos().y) / 2);
    }

    private void drawEvents(Graphics g) {
        for (Node node: graph.getNodes()) {
            drawEventsOnLocation(g, node, (Point) node.getPos().clone());
        }
        for (Edge edge: graph.getEdges()) {
            drawEventsOnLocation(g, edge, calculateCenter(edge));
        }

    }

    private void drawEventsOnLocation(Graphics g, ArmyLocation location, Point pos) {
        if (!location.getEvents().isEmpty()) {
            Point posCopy = (Point) pos.clone();
            pos.x -= 80;
            for (Event event : location.getEvents()) {
                String name = event.getName();
                g.drawString(name, pos.x - 100, pos.y);
                pos.y += 15;
            }
            g.drawLine(posCopy.x - 120, posCopy.y, posCopy.x, posCopy.y);
        }
    }

    private void drawArmiesOnNode(Graphics g, Node node) {
        int offsetChange = 20;
        Point pos = new Point(node.getPos().x - offsetChange - node.getSize() / 2,
                                node.getPos().y - offsetChange - node.getSize());
        for (Army army: node.getArmiesPresent()) {
            Image image = TextureLoader.getInstance().getTexture(
                    "faction" + army.getFaction().getFactionName(), 60, 60);
            g.drawImage(image, pos.x, pos.y, this);
            pos.x += offsetChange;
        }
    }

    private void drawArmiesOnEdge(Graphics g, Edge edge) {
        int offsetChange = 20;
        Point pos = calculateCenter(edge);
        for (Army army : edge.getArmiesPresent()) {
            Image image = TextureLoader.getInstance().getTexture(
                    "faction" + army.getFaction().getFactionName(), 60, 60);
            g.drawImage(image, pos.x, pos.y, this);
            pos.x += offsetChange;
        }
    }

    /**
     * Draw a node.
     * fillOval draws an oval bounded by the given rectangle, not centered at x and y.
     * This is why we subtract the size.
     * @param g Graphics arg.
     * @param node The node to be drawn.
     */
    private void drawNode(Graphics g, Node node) {
        int stringOffset = 5;
        int size = node.getSize();
        Point pos = node.getPos();
        int x = pos.x - size / 2;
        int y = pos.y - size / 2;
        g.setColor(Color.BLUE);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawString(node.getName(), x + node.getSize() + stringOffset, y + node.getSize() / 2 + stringOffset);
        drawArmiesOnNode(g, node);
    }

    /**
     * Draw the border around the selected node. The node itself will be drawn in the drawNode function.
     * @param g Graphics argument.
     */
    private void drawSelectedNode(Graphics g) {
        if (selectedNode == null) {
            return;
        }
        int size = 60;
        Point pos = selectedNode.getPos();
        g.setColor(new Color(253, 16, 36));
        g.drawRect(pos.x - size, pos.y - size, size * 2, size * 2);
    }

    /**
     * Draws all the nodes in the graph.
     * @param g Graphics argument.
     */
    private void drawNodes(Graphics g) {
        drawSelectedNode(g);
        for (Node node: graph.getNodes()) {
            drawNode(g, node);
        }
    }

    /**
     * Draw an edge.
     * @param g Graphics argument.
     * @param edge The edge to be drawn.
     */
    private void drawEdge(Graphics g, Edge edge) {
        List<Node> nodes = edge.getNodes();
        Node node1 = nodes.get(0);
        Node node2 = nodes.get(1);
        if (edge == selectedEdge) {
            g.setColor(new Color(253, 16, 36));
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawLine(node1.getPos().x, node1.getPos().y, node2.getPos().x, node2.getPos().y);
        drawArmiesOnEdge(g, edge);
    }

    /**
     * Draws all the edges in the graph.
     * @param g Graphics argument.
     */
    private void drawEdges(Graphics g) {
        for (Edge edge: graph.getEdges()) {
            drawEdge(g, edge);
        }
    }

    /**
     * Update the view when model is updated. Implements method from Observer interface.
     */
    public void update() {
        repaint();
    }

    /**
     * Getter for selected node.
     * @return The selected node.
     */
    public Node getSelectedNode() {
        return selectedNode;
    }
}
