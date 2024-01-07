package de.famiru.ctriddle.chilly.layer1;

import de.famiru.ctriddle.chilly.game.Board;
import de.famiru.ctriddle.chilly.game.Coordinates;

import java.util.Set;

class GraphCreator {
    Graph create(Board board, int playerX, int playerY) {
        Graph graph = new Graph();
        graph.insertNode(new Node(playerX, playerY));

        insertNode(board, graph, playerX, playerY);

        return graph;
    }

    private void insertNode(Board board, Graph graph, int x, int y) {
        if (board.isExit(x, y)) {
            return;
        }

        if (board.canMoveRight(x, y)) {
            Coordinates coordinates = board.moveRight(x, y);
            Coordinates coin = board.coinAtMoveRight(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateRight(x, y, node);
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveLeft(x, y)) {
            Coordinates coordinates = board.moveLeft(x, y);
            Coordinates coin = board.coinAtMoveLeft(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateLeft(x, y, node);
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveUp(x, y)) {
            Coordinates coordinates = board.moveUp(x, y);
            Coordinates coin = board.coinAtMoveUp(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateUp(x, y, node);
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveDown(x, y)) {
            Coordinates coordinates = board.moveDown(x, y);
            Coordinates coin = board.coinAtMoveDown(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateDown(x, y, node);
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }
    }

    private Node create(Graph graph, Coordinates target, Coordinates coin) {
        Set<Node> presentNodes = graph.getNodesAt(target.x(), target.y());
        Node node;
        if (coin == null) {
            node = new Node(target.x(), target.y());
        } else {
            node = new Node(target.x(), target.y(), coin.x(), coin.y());
        }
        for (Node presentNode : presentNodes) {
            if (presentNode.equals(node)) {
                return presentNode;
            } else {
                node.transferArcs(presentNode);
            }
        }
        return node;
    }
}
