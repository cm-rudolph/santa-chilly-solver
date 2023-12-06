package de.famiru.ctriddle.chilly.layer1;

import de.famiru.ctriddle.chilly.Board;
import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraSolver {
    private static final Logger LOGGER = LogManager.getLogger(DijkstraSolver.class);

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

    public void createGraph() {
        Set<Node> startNodes = graph.getNodesAt(playerX, playerY);
        if (startNodes.size() != 1) {
            throw new IllegalArgumentException("The solver doesn't support start coordinates which are reachable " +
                    "for the player.");
        }

        Node startNode = startNodes.iterator().next();
        Coordinates exit = board.getExit();
        Set<Node> exitNodes = graph.getNodesAt(exit.x(), exit.y());

        List<Node> allNodes = graph.getAllNodes();
        int startNodeIndex = allNodes.indexOf(startNode);
        int exitNodeIndex = allNodes.indexOf(exitNodes.iterator().next());
        LOGGER.info("Start node: {}", startNodeIndex);
        LOGGER.info("Exit node: {}", exitNodeIndex);
        int maxDistance = 0;
        for (int i = 0; i < allNodes.size(); i++) {
            if (i == exitNodeIndex) {
                // exit node has no outgoing arcs. Therefore, Constants.INFINITY
                continue;
            }
            for (int j = 0; j < allNodes.size(); j++) {
                if (j == startNodeIndex) {
                    // start node isn't reachable. Therefore, Constants.INFINITY
                    continue;
                }
                int distance = distance(allNodes.get(i), allNodes.get(j));
                // at this point, the target node contains a path backwards to the start node
                if (distance < allNodes.size() && maxDistance < distance) {
                    maxDistance = distance;
                }
            }
        }
        LOGGER.info("Max reachable distance: {}", maxDistance);
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

            checkAndUpdate(queue, node.getUp(), node);
            checkAndUpdate(queue, node.getDown(), node);
            checkAndUpdate(queue, node.getRight(), node);
            checkAndUpdate(queue, node.getLeft(), node);
        }
        return Constants.INFINITY;
    }

    private void checkAndUpdate(PriorityQueue<Node> queue, Node target, Node node) {
        if (target != null && !target.isVisited()
                && node.getDistance() < Constants.INFINITY // skip nodes that aren't reachable at all
                && target.getDistance() > node.getDistance() + 1) {
            queue.remove(target);
            target.setDistance(node.getDistance() + 1);
            target.setPrevious(node);
            queue.add(target);
        }
    }
}
