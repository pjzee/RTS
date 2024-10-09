package nl.rug.oop.rts.util;

import nl.rug.oop.rts.graph.Edge;
import nl.rug.oop.rts.graph.Graph;
import nl.rug.oop.rts.graph.Node;
import nl.rug.oop.rts.simulation.Army;
import nl.rug.oop.rts.simulation.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

/**
 * JsonExporter class.
 */
public class JSONExporter {

    private static final String COMMA_NEWLINE = ",\n";

    public JSONExporter() {
    }

    /**
     * Function to write a Graph class to json.
     *
     * @param graph The graph.
     * @param path The path to the file.
     */
    public void graphToJson(Graph graph, Path path) {

        String json = "{\n  \"Nodes\": [\n" +
                graph.getNodes().stream()
                        .map(this::nodeToJson)
                        .collect(Collectors.joining(COMMA_NEWLINE)) +
                "\n  ],\n  \"Edges\": [\n" +
                graph.getEdges().stream()
                        .map(this::edgeToJson)
                        .collect(Collectors.joining(COMMA_NEWLINE)) +
                "\n  ]\n}";

        writeString(path, json);
    }

    /**
     * Function to write a Node class to json.
     *
     * @param node The node.
     * @return The string of json.
     */
    public String nodeToJson(Node node) {
        StringBuilder json = new StringBuilder();
        json.append("    {\n      \"Id\": ").append(node.getId())
                .append(",\n      \"Name\": \"").append(node.getName())
                .append("\",\n      \"Armies\": ");

        if (node.getArmiesPresent().isEmpty()) {
            json.append("[]");
        } else {
            json.append("[\n");
            json.append(node.getArmiesPresent().stream()
                    .map(this::armyToJson)
                    .collect(Collectors.joining(COMMA_NEWLINE)));
            json.append("\n      ]");
        }

        json.append(",\n      \"Events\": ");

        if (node.getEvents().isEmpty()) {
            json.append("[]");
        } else {
            json.append("[\n");
            json.append(node.getEvents().stream()
                    .map(event -> "        \"" + event.getName() + "\"")
                    .collect(Collectors.joining(COMMA_NEWLINE)));
            json.append("\n      ]");
        }

        json.append("\n    }");
        return json.toString();
    }

    /**
     * Function to write an Edge class to json.
     *
     * @param edge The edge.
     * @return The string of json.
     */
    public String edgeToJson(Edge edge) {
        StringBuilder json = new StringBuilder();
        json.append("    {\n      \"Id\": ").append(edge.getId())
                .append(",\n      \"Name\": \"").append(edge.getName())
                .append("\",\n      \"Node1\": ").append(edge.getNodes().get(0).getId())
                .append(",\n      \"Node2\": ").append(edge.getNodes().get(1).getId())
                .append(",\n      \"Armies\": ");

        if (edge.getArmiesPresent().isEmpty()) {
            json.append("[]");
        } else {
            json.append("[\n");
            json.append(edge.getArmiesPresent().stream()
                    .map(this::armyToJson)
                    .collect(Collectors.joining(COMMA_NEWLINE)));
            json.append("\n      ]");
        }

        json.append(",\n      \"Events\": ");

        if (edge.getEvents().isEmpty()) {
            json.append("[]");
        } else {
            json.append("[\n");
            json.append(edge.getEvents().stream()
                    .map(event -> "        \"" + event.getName() + "\"")
                    .collect(Collectors.joining(COMMA_NEWLINE)));
            json.append("\n      ]");
        }

        json.append("\n    }");
        return json.toString();
    }

    /**
     * Function to write an Army class to json.
     *
     * @param army The army.
     * @return The string of json.
     */
    public String armyToJson(Army army) {
        StringBuilder json = new StringBuilder();
        json.append("        {\n          \"Faction\": \"").append(army.getFaction().getFactionName())
                .append("\",\n          \"Team\": ").append(army.getFaction().getTeamNum())
                .append(",\n          \"Units\": ");

        if (army.getUnits().isEmpty()) {
            json.append("[]");
        } else {
            json.append("[\n");
            json.append(army.getUnits().stream()
                    .map(this::unitToJson)
                    .collect(Collectors.joining(COMMA_NEWLINE)));
            json.append("\n          ]");
        }

        json.append("\n        }");
        return json.toString();
    }

    /**
     * Function to write an Unit class to json.
     *
     * @param unit The unit.
     * @return The string of json.
     */
    public String unitToJson(Unit unit) {
        return "            {\n              \"Name\": \"" + unit.getName() + "\",\n              " +
                "\"Strength\": " + unit.getDamage() + ",\n              \"Health\": "
                + unit.getHealth() + "\n            }";
    }

    /**
     * Function to write a string to a file.
     *
     * @param path The path to the file.
     * @param s The string to be written to the file.
     */
    public void writeString(Path path, String s) {
        try {
            Files.writeString(path, s, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error writing string: " + e.getMessage());
        }
    }
}
