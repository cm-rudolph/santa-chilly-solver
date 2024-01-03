package de.famiru.ctriddle.chilly.layer1;

import de.famiru.ctriddle.chilly.Board;
import de.famiru.ctriddle.chilly.Coordinates;

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
            Board.CoordinatesAndDistance cad = board.moveRight(x, y);
            Coordinates coordinates = cad.coordinates();
            Coordinates coin = board.coinAtMoveRight(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateRight(x, y, node, cad.distance());
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveLeft(x, y)) {
            Board.CoordinatesAndDistance cad = board.moveLeft(x, y);
            Coordinates coordinates = cad.coordinates();
            Coordinates coin = board.coinAtMoveLeft(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateLeft(x, y, node, cad.distance());
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveUp(x, y)) {
            Board.CoordinatesAndDistance cad = board.moveUp(x, y);
            Coordinates coordinates = cad.coordinates();
            Coordinates coin = board.coinAtMoveUp(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateUp(x, y, node, cad.distance());
            if (graph.insertNode(node)) {
                insertNode(board, graph, coordinates.x(), coordinates.y());
            }
        }

        if (board.canMoveDown(x, y)) {
            Board.CoordinatesAndDistance cad = board.moveDown(x, y);
            Coordinates coordinates = cad.coordinates();
            Coordinates coin = board.coinAtMoveDown(x, y).orElse(null);
            Node node = create(graph, coordinates, coin);
            graph.updateDown(x, y, node, cad.distance());
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
