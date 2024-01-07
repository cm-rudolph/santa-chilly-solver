package de.famiru.ctriddle.chilly.layer1;

import de.famiru.ctriddle.chilly.game.Board;
import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.game.Coordinates;
import de.famiru.ctriddle.chilly.layer2.Matrix;

import java.util.*;

public class DijkstraSolver {
    private final Board board;
    private final Graph graph;
    private final int playerX;
    private final int playerY;

    public DijkstraSolver(Board board, int playerX, int playerY) {
        this.board = board;
        this.playerX = playerX;
        this.playerY = playerY;
        this.graph = new GraphCreator().create(board, playerX, playerY);
    }

    public Matrix createAtspMatrix() {
        Node startNode = getStartNode();
        Set<Node> exitNodes = getExitNodes();

        List<Node> allNodes = findAndMarkAllRelevantNodes(startNode, exitNodes);

        Matrix matrix = createMatrix(allNodes, startNode, exitNodes);
        transformFromGtspToAtsp(matrix);
        connectExitWithStart(matrix, startNode, exitNodes);
        return matrix;
    }

    private List<Node> findAndMarkAllRelevantNodes(Node startNode, Set<Node> exitNodes) {
        List<Node> allNodes = new ArrayList<>();
        allNodes.add(startNode);
        exitNodes.stream()
                .filter(node -> !node.hasCoin())
                .forEach(allNodes::add);
        allNodes.addAll(graph.getAllCoinNodes());
        for (int i = 0; i < allNodes.size(); i++) {
            Node node = allNodes.get(i);
            node.setIndex(i);
        }
        return allNodes;
    }

    private void transformFromGtspToAtsp(Matrix matrix) {
        List<List<Node>> clusters = graph.getCoinNodeClusters();
        for (List<Node> cluster : clusters) {
            // move all outgoing arcs to preceding node in cluster cycle
            for (int i = 0; i < cluster.size() - 1; i++) {
                Node nodeI1 = cluster.get(i);
                Node nodeI2 = cluster.get((i + 1) % cluster.size());
                matrix.swapRows(nodeI1.getIndex(), nodeI2.getIndex());
            }

            for (int i = 0; i < cluster.size(); i++) {
                Node nodeI = cluster.get(i);
                for (int j = 0; j < cluster.size(); j++) {
                    Node nodeJ = cluster.get(j);

                    if (i == j) {
                        // restore self connect zeroes
                        matrix.setPath(nodeI.getIndex(), nodeJ.getIndex(), "");
                        matrix.setEntry(nodeI.getIndex(), nodeJ.getIndex(), 0);
                    } else if (((i + 1) % cluster.size()) == j) {
                        // place an arc of zero weight to the next node
                        matrix.setPath(nodeI.getIndex(), nodeJ.getIndex(), "cluster shortcut");
                        matrix.setEntry(nodeI.getIndex(), nodeJ.getIndex(), 0);
                    } else {
                        // never use other connections within cluster
                        matrix.setPath(nodeI.getIndex(), nodeJ.getIndex(), "cluster disconnect");
                        matrix.setEntry(nodeI.getIndex(), nodeJ.getIndex(), Constants.INFINITY);
                    }
                }
            }
        }
    }

    private void connectExitWithStart(Matrix matrix, Node startNode, Set<Node> exitNodes) {
        int startNodeIndex = startNode.getIndex();
        for (Node exitNode : exitNodes) {
            matrix.setPath(exitNode.getIndex(), startNodeIndex, Constants.EXIT_TO_START_PATH);
            matrix.setEntry(exitNode.getIndex(), startNodeIndex, 0);
        }
    }

    private Node getStartNode() {
        Set<Node> startNodes = graph.getNodesAt(playerX, playerY);
        for (Node startNode : startNodes) {
            if (!startNode.hasCoin()) {
                return startNode;
            }
        }
        throw new IllegalArgumentException("There is no start node.");
    }

    private Set<Node> getExitNodes() {
        Coordinates exit = board.getExit();
        return graph.getNodesAt(exit.x(), exit.y());
    }

    private Matrix createMatrix(List<Node> allNodes, Node startNode, Set<Node> exitNodes) {
        Matrix result = new Matrix(allNodes.size());
        for (int i = 0; i < allNodes.size(); i++) {
            Node nodeI = allNodes.get(i);
            boolean isExitNode = exitNodes.contains(nodeI);
            if (isExitNode) {
                result.setDescription(i, "X " + nodeI.getDescription());
            } else if (startNode.equals(nodeI)) {
                result.setDescription(i, "S " + nodeI.getDescription());
            } else {
                result.setDescription(i, nodeI.getDescription());
            }

            for (int j = 0; j < allNodes.size(); j++) {
                Node nodeJ = allNodes.get(j);
                int distance = distance(nodeI, nodeJ);
                result.setEntry(i, j, distance);
                result.setPath(i, j, nodeJ.getPath());
            }
        }
        return result;
    }

    private int distance(Node start, Node target) {
        graph.reset();

        start.setDistance(0);
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getDistance));
        queue.addAll(graph.getAllNodes());

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            node.markVisited();

            // this is not optimal. If target is equal to node.getUp(), node.getDown(), ..., we already found the
            // target node with the optimal path. But... It is fast enough. Spend some CPU cycles for easier code.
            if (target.equals(node)) {
                return target.getDistance();
            }

            checkAndUpdate(queue, node.getUp(), node, 'U');
            checkAndUpdate(queue, node.getDown(), node, 'D');
            checkAndUpdate(queue, node.getRight(), node, 'R');
            checkAndUpdate(queue, node.getLeft(), node, 'L');
        }
        return Constants.INFINITY;
    }

    private void checkAndUpdate(PriorityQueue<Node> queue, Node target, Node node, char direction) {
        if (target != null && !target.isVisited()
                && node.getDistance() < Constants.INFINITY // skip nodes that aren't reachable at all
                && target.getDistance() > node.getDistance() + 1) {
            queue.remove(target);
            target.setDistance(node.getDistance() + 1);
            target.setPrevious(node);
            target.setPath(node.getPath() + direction);
            queue.add(target);
        }
    }
}
