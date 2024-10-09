package nl.rug.oop.rts.swing;

import nl.rug.oop.rts.graph.ArmyLocation;
import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.Simulation;
import nl.rug.oop.rts.simulation.events.Event;
import nl.rug.oop.rts.simulation.factions.*;
import nl.rug.oop.rts.util.JSONExporter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;

/**
 * Main frame class.
 */
public class Frame extends JFrame {
    private int width = 1400;
    private int height = 900;
    private MainPanel mainPanel;
    private OptionsMenu optionsMenu;

    /**
     * Constructor for Frame.
     * @param graph The graph object.
     * @param sim The simulation object.
     */
    public Frame(Graph graph, Simulation sim) {
        //set up the window.
        this.setTitle(("OMG its a game!"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setLayout(new BorderLayout());

        ArrayList<JButton> buttons = addButtons(graph, sim);
        addPanels(graph, buttons);
    }

    /**
     * Add the two panels to the frame.
     * The two panels are the options menu on the left and the main panel which will be on the right.
     * They will be configured using a JSplitPane.
     * @param graph The graph object.
     * @param buttons The button list that is added to a menu bar in the main panel.
     */
    protected void addPanels(Graph graph, ArrayList<JButton> buttons) {
        OptionsMenu optionsMenu = new OptionsMenu();
        MainPanel mainPanel = new MainPanel(graph, buttons, optionsMenu);

        graph.addObserver(mainPanel);
        optionsMenu.addObserver(mainPanel);

        this.mainPanel = mainPanel;
        this.optionsMenu = optionsMenu;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionsMenu, mainPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        this.add(splitPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Getter for the height of the window.
     * @return the height of the window.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter for the width of the window.
     * @return The width of the window.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Create all the buttons used in the menu bar in the main panel.
     * @param graph The graph object.
     * @param sim The simulation object.
     * @return A list of the buttons.
     */
    private ArrayList<JButton> createButtons(Graph graph, Simulation sim) {
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> graph.createNode(new Point(getWidth() / 2, getHeight() / 2)));

        JButton removeNodeButton = getRemoveNodeButton(graph);
        JButton addEdgeButton = getAddEdgeButton(graph);
        JButton removeEdgeButton = getRemoveEdgeButton(graph);
        JButton addArmyButton = getAddArmyButton(graph);
        JButton simulateStepButton = getSimulateStepButton(sim);
        JButton addEventButton = getAddEventButton();
        JButton toJsonButton = getJsonButton(graph);

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(addNodeButton);
        buttons.add(removeNodeButton);
        buttons.add(addEdgeButton);
        buttons.add(removeEdgeButton);
        buttons.add(addArmyButton);
        buttons.add(simulateStepButton);
        buttons.add(addEventButton);
        buttons.add(toJsonButton);
        return buttons;
    }

    private JButton getRemoveNodeButton(Graph graph) {
        JButton removeNodeButton = new JButton("Remove Node");
        removeNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.removeNode(mainPanel.getSelectedNode());
                mainPanel.clearSelectedNode();
            }
        });
        return removeNodeButton;
    }

    private JButton getAddEdgeButton(Graph graph) {
        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.setWaiting(true);
            }
        });
        return addEdgeButton;
    }

    private JButton getRemoveEdgeButton(Graph graph) {
        JButton removeEdgeButton = new JButton("Remove Edge");
        removeEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.removeEdge(mainPanel.getSelectedEdge());
                mainPanel.clearSelectedEdge();
            }
        });
        return removeEdgeButton;
    }

    private JButton getJsonButton(Graph graph) {
        JButton toJsonButton = new JButton("To JSON");
        toJsonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
                chooser.setFileFilter(filter);

                int returnVal = chooser.showSaveDialog(Frame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getAbsolutePath();
                    if (!filename.endsWith(".json")) {
                        filename += ".json";
                    }
                    Path path = Paths.get(filename);
                    JSONExporter exporter = new JSONExporter();
                    exporter.graphToJson(graph, path);
                }
            }
        });
        return toJsonButton;
    }

    private JButton getAddEventButton() {
        JButton addEventButton = new JButton("Add event");
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> eventNames = new ArrayList<>();
                eventNames.add("Fog event");
                eventNames.add("Rebellion event");
                eventNames.add("Change of mind event");
                Object[] options = eventNames.toArray(new Object[0]);
                String selectedEventName = JOptionPane.showInputDialog(
                        null,
                        "Select an type for this event:",
                        "Event selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        eventNames.get(0)
                ).toString();
                ArmyLocation selectedLoc = mainPanel.getSelectedLocation();
                if (selectedEventName != null && selectedLoc != null) {
                    selectedLoc.addEvent(Event.createEvent(selectedEventName));
                }
            }
        });
        return addEventButton;
    }

    private JButton getAddArmyButton(Graph graph) {
        JButton addArmyButton = new JButton("Add Army");
        addArmyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> factionNames = new ArrayList<>();
                factionNames.add("Men");
                factionNames.add("Dwarves");
                factionNames.add("Elves");
                factionNames.add("Mordor");
                factionNames.add("Isengard");
                Object[] options = factionNames.toArray(new Object[0]);
                String selectedFactionName = JOptionPane.showInputDialog(
                        null,
                        "Select a faction for this army:",
                        "Faction selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        factionNames.get(0)
                ).toString();

                if (selectedFactionName != null && mainPanel.getSelectedNode() != null) {
                    Faction faction = getSelectedFaction(selectedFactionName);
                    Army army = new Army(10, faction, mainPanel.getSelectedNode());
                    graph.addArmy(mainPanel.getSelectedNode(), army);
                }
            }
        });
        return addArmyButton;
    }

    private JButton getSimulateStepButton(Simulation sim) {
        JButton simulateStepButton = new JButton("Simulate step");
        simulateStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.simulateStep();
            }
        });
        return simulateStepButton;
    }

    private Faction getSelectedFaction(String selectedFactionName) {
        return switch (selectedFactionName) {
            case "Men" -> new Men();
            case "Dwarves" -> new Dwarves();
            case "Elves" -> new Elves();
            case "Mordor" -> new Mordor();
            case "Isengard" -> new Isengard();
            default -> null;
        };
    }

    /**
     * Create and configure the four buttons for the menu bar for the main panel.
     * @param graph The graph object.
     * @param sim The simulation object.
     * @return A list with all the buttons.
     */
    protected ArrayList<JButton> addButtons(Graph graph, Simulation sim) {
        ArrayList<JButton> buttons = createButtons(graph, sim);
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for (JButton button: buttons) {
            toolBar.add(button);
        }

        this.add(toolBar, BorderLayout.NORTH);
        return buttons;
    }

}
