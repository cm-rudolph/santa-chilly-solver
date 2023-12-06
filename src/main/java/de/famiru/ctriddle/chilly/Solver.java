package de.famiru.ctriddle.chilly;

import de.famiru.ctriddle.chilly.layer1.AllNodes;
import de.famiru.ctriddle.chilly.layer1.Node;

import java.util.Set;

public class Solver {
    private final Board board;
    private final AllNodes allNodes;
    private int playerX;
    private int playerY;

    public Solver(Board board, int playerX, int playerY) {
        this.board = board;
        this.playerX = playerX;
        this.playerY = playerY;
        this.allNodes = new AllNodes();
        allNodes.insertNode(new Node(playerX, playerY));
    }

    public void createGraph() {
        insertNode(playerX, playerY);
        /*Set<Node> startNodes = allNodes.getNodesAt(playerX, playerY);
        if (startNodes.size() != 1) {
            throw new IllegalArgumentException("The solver doesn't support start coordinates which are reachable " +
                    "for the player.");
        }
        Coordinates exit = board.getExit();
        Set<Node> exitNodes = allNodes.getNodesAt(exit.x(), exit.y());*/

    }

    private void insertNode(int x, int y) {
        if (board.isExit(x, y)) {
            return;
        }

        if (board.canMoveRight(x, y)) {
            Coordinates coordinates = board.moveRight(x, y);
            Coordinates coin = board.coinAtMoveRight(x, y).orElse(null);
            Node node = create(coordinates, coin);
            allNodes.updateRight(x, y, node);
            if (allNodes.insertNode(node)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveLeft(x, y)) {
            Coordinates coordinates = board.moveLeft(x, y);
            Coordinates coin = board.coinAtMoveLeft(x, y).orElse(null);
            Node node = create(coordinates, coin);
            allNodes.updateLeft(x, y, node);
            if (allNodes.insertNode(node)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveUp(x, y)) {
            Coordinates coordinates = board.moveUp(x, y);
            Coordinates coin = board.coinAtMoveUp(x, y).orElse(null);
            Node node = create(coordinates, coin);
            allNodes.updateUp(x, y, node);
            if (allNodes.insertNode(node)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveDown(x, y)) {
            Coordinates coordinates = board.moveDown(x, y);
            Coordinates coin = board.coinAtMoveDown(x, y).orElse(null);
            Node node = create(coordinates, coin);
            allNodes.updateDown(x, y, node);
            if (allNodes.insertNode(node)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }
    }

    private Node create(Coordinates target, Coordinates coin) {
        Set<Node> presentNodes = allNodes.getNodesAt(target.x(), target.y());
        Node node;
        if (coin == null) {
            node = new Node(target.x(), target.y());
        } else {
            node = new Node(target.x(), target.y(), coin.x(), coin.y());
        }
        for (Node presentNode : presentNodes) {
            if (presentNode.equals(node)) {
                return presentNode;
            }
        }
        return node;
    }
}
