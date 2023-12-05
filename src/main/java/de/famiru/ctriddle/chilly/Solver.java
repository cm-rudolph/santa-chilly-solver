package de.famiru.ctriddle.chilly;

import de.famiru.ctriddle.chilly.layer1.AllNodes;
import de.famiru.ctriddle.chilly.layer1.Node;

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
    }

    private void insertNode(int x, int y) {
        if (board.isExit(x, y)) {
            return;
        }

        if (board.canMoveRight(x, y)) {
            Coordinates coordinates = board.moveRight(x, y);
            Coordinates coin = board.coinAtMoveRight(x, y).orElse(null);
            if (createAndInsert(coordinates, coin)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveLeft(x, y)) {
            Coordinates coordinates = board.moveLeft(x, y);
            Coordinates coin = board.coinAtMoveLeft(x, y).orElse(null);
            if (createAndInsert(coordinates, coin)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveUp(x, y)) {
            Coordinates coordinates = board.moveUp(x, y);
            Coordinates coin = board.coinAtMoveUp(x, y).orElse(null);
            if (createAndInsert(coordinates, coin)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveDown(x, y)) {
            Coordinates coordinates = board.moveDown(x, y);
            Coordinates coin = board.coinAtMoveDown(x, y).orElse(null);
            if (createAndInsert(coordinates, coin)) {
                insertNode(coordinates.x(), coordinates.y());
            }
        }
    }

    private boolean createAndInsert(Coordinates target, Coordinates coin) {
        Node node;
        if (coin == null) {
            node = new Node(target.x(), target.y());
        } else {
            node = new Node(target.x(), target.y(), coin.x(), coin.y());
        }

        return allNodes.insertNode(node);
    }
}
