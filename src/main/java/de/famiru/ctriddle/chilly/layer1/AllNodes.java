package de.famiru.ctriddle.chilly.layer1;

import java.util.*;

public class AllNodes {
    // wenn hier ein Knoten eingefügt wird und die Liste nicht leer ist, dann werden die ausgehenden Kanten des
    // vorhandenen Knotens an den neu eingefügten ergänzt. Da der Knoten bereits besucht wurde, kann die Rekursion
    // dann abbrechen.
    private final Map<Coordinates, Set<Node>> nodes = new HashMap<>();

    // wenn hier ein Knoten eingefügt wird und die Liste nicht leer ist, dann wird der neue Knoten einfach ergänzt
    private final Map<Coordinates, Set<Node>> coinNodes = new HashMap<>();

    /**
     * @return true, if the node is the first one at its coordinates
     */
    public boolean insertNode(Node node) {
        Coordinates coordinates = new Coordinates(node.getX(), node.getY());
        Set<Node> set = nodes.computeIfAbsent(coordinates, c -> new HashSet<>());

        boolean firstNode = set.isEmpty();

        if (set.add(node) && !firstNode) {
            transferArcs(set.iterator().next(), node);
        }
        if (node.hasCoin()) {
            insertCoinNode(node);
        }

        return firstNode;
    }

    private void insertCoinNode(Node node) {
        Coordinates coordinates = new Coordinates(node.getCoinX(), node.getCoinY());
        Set<Node> set = coinNodes.computeIfAbsent(coordinates, c -> new HashSet<>());
        set.add(node);
    }

    private void transferArcs(Node source, Node target) {
        target.setUp(source.getUp());
        target.setDown(source.getDown());
        target.setRight(source.getRight());
        target.setLeft(source.getLeft());
    }

    public void updateUp(int x, int y, Node node) {
        Coordinates coordinates = new Coordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setUp(node);
        }
    }

    public void updateDown(int x, int y, Node node) {
        Coordinates coordinates = new Coordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setDown(node);
        }
    }

    public void updateRight(int x, int y, Node node) {
        Coordinates coordinates = new Coordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setRight(node);
        }
    }

    public void updateLeft(int x, int y, Node node) {
        Coordinates coordinates = new Coordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setLeft(node);
        }
    }
}
