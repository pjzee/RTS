package nl.rug.oop.rts.swing;

import nl.rug.oop.rts.graph.Edge;
import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.graph.Node;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A class that handles input from the mouse.
 */
public class Mouse extends MouseAdapter {
    private final Graph graph;
    private final MainPanel panel;
    private Node selectedNode;

    /**
     * Constructor for the Mouse class.
     * @param graph The graph of nodes.
     * @param panel The panel that will be interacted with.
     */
    public Mouse(Graph graph, MainPanel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    private Node clickedNode(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        for (Node node : graph.getNodes()) {
            // calculate distance to node
            Point pos = node.getPos();
            double distance = Math.sqrt(Math.pow(mouseX - pos.x, 2) + Math.pow(mouseY - pos.y, 2));
            // check if distance smaller equal than radius, size / 2.
            if (distance <= (double) node.getSize() / 2) {
                return node;
            }
        }
        return null;
    }

    private Edge clickedEdge(MouseEvent e) {
        int range = 10;
        int mouseX = e.getX();
        int mouseY = e.getY();
        for (Edge edge : graph.getEdges()) {
            // calculate line equation between nodes
            List<Node> nodes = edge.getNodes();
            Point pos1 = nodes.get(0).getPos();
            Point pos2 = nodes.get(1).getPos();
            double dx = pos1.x - pos2.x;
            double dy = pos1.y - pos2.y;
            double a = dy / dx;
            double c = pos1.y - pos1.x * a;
            double b = -1;
            double distance = Math.abs(a*mouseX + b*mouseY + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            if (distance < range) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Handle what to do when the mouse gets clicked.
     * First check distance to every node to see if its within the radius of each node.
     * @param e The event to be processed.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Node clickedNode = clickedNode(e);
        Edge clickedEdge = clickedEdge(e);
        if (clickedNode != null) {
            if (panel.getWaiting()) {
                graph.createEdge(panel.getSelectedNode(), clickedNode);
                panel.clearSelectedNode();
                panel.update();
                panel.setWaiting(false);
                return;
            }
            panel.clearSelectedEdge();
            panel.setSelectedNode(clickedNode);
            panel.update();
            selectedNode = clickedNode;
        } else if (clickedEdge != null ) {
            panel.clearSelectedNode();
            panel.setSelectedEdge(clickedEdge);
            panel.update();
        } else {
            panel.clearSelectedNode();
            panel.clearSelectedEdge();
            panel.setWaiting(false);
            selectedNode = null;
        }
    }

    /**
     * Handle what to do when mouse is released.
     * Sets selectedNode to null to get dragging to work.
     * @param e The event to be processed.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        selectedNode = null;
    }

    /**
     * Handle what to do when mouse is dragged.
     * If a node is selected, move it.
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Node clicked = clickedNode(e);
        if (clicked != null) {
            graph.moveNode(clicked, e.getPoint());
            panel.setSelectedNode(clicked);
        }
    }
    
}
