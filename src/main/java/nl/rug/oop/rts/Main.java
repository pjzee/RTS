package nl.rug.oop.rts;

import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.simulation.Simulation;
import nl.rug.oop.rts.swing.Frame;

/**
 * Main class of the application. Add more details here.
 */
public class Main {

    /**
     * Main function. Add more details here.
     *
     * @param args Commandline arguments.
     */
    public static void main(String[] args) {
        //FlatDarculaLaf.setup(); // Dark mode
        Graph graph = new Graph();
        // TEMP: initializing graph
        // END TEMP
        Simulation sim = new Simulation(graph);
        Frame frame = new Frame(graph, sim);
    }
}
