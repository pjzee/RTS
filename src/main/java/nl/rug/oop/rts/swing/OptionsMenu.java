package nl.rug.oop.rts.swing;

import nl.rug.oop.rts.Observer;
import nl.rug.oop.rts.Subject;
import nl.rug.oop.rts.graph.Edge;
import nl.rug.oop.rts.graph.Node;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Options menu class. Displays relevant information about the selected node or edge and allows the user to change
 * some properties of the selected node or edge.
 */
public class OptionsMenu extends JPanel implements Subject {
    private JLabel nodeNameLabel, edgeNameLabel, edgeStartLabel, edgeEndLabel, defaultMessage;
    private JTextField nodeNameField, edgeNameField, edgeStartField, edgeEndField;
    private Node selectedNode;
    private Edge selectedEdge;
    private List<Observer> observers = new ArrayList<>();

    /**
     * Constructor. Sets a layout and configures all text fields and labels.
     */
    public OptionsMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents();
        prepareComponents();
        addNodeListener();
        addEdgeListener();
    }

    /**
     * Add listeners to the node text field to update the name of the node. Notifies observer, main panel.
     */
    private void addNodeListener() {
        nodeNameField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateNodeName() {
                String s = nodeNameField.getText().trim();
                if (selectedNode != null) {
                    selectedNode.setName(s);
                    updateObservers();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateNodeName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateNodeName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateNodeName();
            }
        });
    }

    /**
     * Add listeners to the edge text field to update the name of the edge. Notifies observer, main panel.
     */
    private void addEdgeListener() {
        edgeNameField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateEdgeName() {
                String s = edgeNameField.getText().trim();
                if (selectedEdge != null) {
                    selectedEdge.setName(s);
                    updateObservers();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEdgeName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEdgeName();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEdgeName();
            }
        });
    }

    /**
     * Initialize all text fields and labels.
     */
    private void initializeComponents() {
        defaultMessage = new JLabel("Nothing is currently selected.");
        nodeNameLabel = new JLabel("Node name:");
        edgeNameLabel = new JLabel("Edge name:");
        nodeNameField = new JTextField(1);
        edgeNameField = new JTextField(1);

        edgeStartLabel = new JLabel("Start node name:");
        edgeEndLabel = new JLabel("End node name:");
        edgeStartField = new JTextField(1);
        edgeEndField = new JTextField(1);
    }

    /**
     * Prepare all the text fields and labels.
     */
    private void prepareComponents() {
        setVisibility();
        setFonts();
        edgeStartField.setEditable(false);
        edgeEndField.setEditable(false);
        setSizes();
        add(defaultMessage);
        add(nodeNameLabel);
        add(nodeNameField);
        add(edgeNameLabel);
        add(edgeNameField);
        add(edgeStartLabel);
        add(edgeStartField);
        add(edgeEndLabel);
        add(edgeEndField);
    }

    /**
     * Set visibility of all the text fields and labels.
     */
    private void setVisibility() {
        nodeNameLabel.setVisible(false);
        edgeNameLabel.setVisible(false);
        nodeNameField.setVisible(false);
        edgeNameField.setVisible(false);
        edgeStartLabel.setVisible(false);
        edgeEndLabel.setVisible(false);
        edgeStartField.setVisible(false);
        edgeEndField.setVisible(false);
    }

    /**
     * Set the font for all the text fields and labels.
     */
    private void setFonts() {
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        nodeNameLabel.setFont(font);
        edgeNameLabel.setFont(font);
        nodeNameField.setFont(font);
        edgeNameField.setFont(font);
        edgeStartLabel.setFont(font);
        edgeEndLabel.setFont(font);
        edgeStartField.setFont(font);
        edgeEndField.setFont(font);

        defaultMessage.setFont(font);
    }

    /**
     * Set the sizes for all the text fields.
     */
    private void setSizes() {
        nodeNameField.setMaximumSize(new Dimension(1400, 40));
        edgeNameField.setMaximumSize(new Dimension(1400, 40));
        edgeStartField.setMaximumSize(new Dimension(1400, 40));
        edgeEndField.setMaximumSize(new Dimension(1400, 40));
    }

    /**
     * Select a new node. Set all edge fields and the default field to invisible and all the node fields to visible.
     * @param node The node that will be selected.
     */
    public void selectNode(Node node) {
        selectedNode = node;

        edgeStartLabel.setVisible(false);
        edgeEndLabel.setVisible(false);
        edgeStartField.setVisible(false);
        edgeEndField.setVisible(false);
        edgeNameLabel.setVisible(false);
        edgeNameField.setVisible(false);
        defaultMessage.setVisible(false);

        nodeNameField.setText(node.getName());
        nodeNameLabel.setVisible(true);
        nodeNameField.setVisible(true);
    }

    /**
     * Select a new edge. Set all node fields and the default field to invisible and all the edge fields to visible.
     * @param edge The edge that will be selected.
     */
    public void selectEdge(Edge edge) {
        selectedEdge = edge;

        nodeNameField.setVisible(false);
        nodeNameLabel.setVisible(false);
        defaultMessage.setVisible(false);

        edgeNameField.setText(edge.getName());
        edgeStartField.setText(edge.getNodes().get(0).getName());
        edgeEndField.setText(edge.getNodes().get(1).getName());

        edgeNameLabel.setVisible(true);
        edgeNameField.setVisible(true);
        edgeStartField.setVisible(true);
        edgeEndField.setVisible(true);
        edgeStartLabel.setVisible(true);
        edgeEndLabel.setVisible(true);
    }

    /**
     * Set all the node and edge fields to invisible and set the default field to visible.
     */
    public void deselectAll() {
        selectedNode = null;
        selectedEdge = null;

        edgeStartLabel.setVisible(false);
        edgeEndLabel.setVisible(false);
        edgeStartField.setVisible(false);
        edgeEndField.setVisible(false);
        edgeNameLabel.setVisible(false);
        edgeNameField.setVisible(false);
        nodeNameField.setVisible(false);
        nodeNameLabel.setVisible(false);

        defaultMessage.setVisible(true);
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
}
