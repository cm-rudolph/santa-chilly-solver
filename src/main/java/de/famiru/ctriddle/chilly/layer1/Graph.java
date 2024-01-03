package de.famiru.ctriddle.chilly.layer1;

import java.util.*;

class Graph {
    // wenn hier ein Knoten eingefügt wird und die Liste nicht leer ist, dann werden die ausgehenden Kanten des
    // vorhandenen Knotens an den neu eingefügten ergänzt. Da der Knoten bereits besucht wurde, kann die Rekursion
    // dann abbrechen.
    private final Map<GraphCoordinates, Set<Node>> nodes = new HashMap<>();

    // wenn hier ein Knoten eingefügt wird und die Liste nicht leer ist, dann wird der neue Knoten einfach ergänzt
    private final Map<GraphCoordinates, Set<Node>> coinNodes = new HashMap<>();

    public Set<Node> getNodesAt(int x, int y) {
        return Set.copyOf(nodes.getOrDefault(new GraphCoordinates(x, y), Set.of()));
    }

    /**
     * @return true, if the node is the first one at its coordinates
     */
    public boolean insertNode(Node node) {
        GraphCoordinates coordinates = new GraphCoordinates(node.getX(), node.getY());
        Set<Node> set = nodes.computeIfAbsent(coordinates, c -> new HashSet<>());

        boolean firstNode = set.isEmpty();

        if (set.add(node) && !firstNode) {
            node.transferArcs(set.iterator().next());
        }
        if (node.hasCoin()) {
            insertCoinNode(node);
        }

        return firstNode;
    }

    private void insertCoinNode(Node node) {
        GraphCoordinates coordinates = new GraphCoordinates(node.getCoinX(), node.getCoinY());
        Set<Node> set = coinNodes.computeIfAbsent(coordinates, c -> new HashSet<>());
        set.add(node);
    }

    public void updateUp(int x, int y, Node node, int distance) {
        GraphCoordinates coordinates = new GraphCoordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setUp(node);
            currentNode.setCostUp(distance);
        }
    }

    public void updateDown(int x, int y, Node node, int distance) {
        GraphCoordinates coordinates = new GraphCoordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setDown(node);
            currentNode.setCostDown(distance);
        }
    }

    public void updateRight(int x, int y, Node node, int distance) {
        GraphCoordinates coordinates = new GraphCoordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setRight(node);
            currentNode.setCostRight(distance);
        }
    }

    public void updateLeft(int x, int y, Node node, int distance) {
        GraphCoordinates coordinates = new GraphCoordinates(x, y);
        Set<Node> set = nodes.get(coordinates);
        for (Node currentNode : set) {
            currentNode.setLeft(node);
            currentNode.setCostLeft(distance);
        }
    }

    public List<Node> getAllNodes() {
        return nodes.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    public List<Node> getAllCoinNodes() {
        return coinNodes.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    public List<List<Node>> getCoinNodeClusters() {
        return coinNodes.values().stream()
                .filter(set -> set.size() > 1)
                .map(set -> (List<Node>) new ArrayList<>(set))
                .toList();
    }

    public void reset() {
        nodes.values().stream()
                .flatMap(Collection::stream)
                .forEach(Node::reset);
    }
}
