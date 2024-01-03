package de.famiru.ctriddle.chilly.layer1;

import de.famiru.ctriddle.chilly.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter(AccessLevel.MODULE)
class Node {
    private final int x;
    private final int y;
    private final int coinX;
    private final int coinY;
    private Node up;
    private Node right;
    private Node down;
    private Node left;
    private int distance;
    private Node previous;
    private String path;
    private boolean visited;
    private int index;

    public Node(int x, int y) {
        this(x, y, -1, -1);
    }

    public Node(int x, int y, int coinX, int coinY) {
        this.x = x;
        this.y = y;
        this.coinX = coinX;
        this.coinY = coinY;
        this.index = -1;
        reset();
    }

    public boolean hasCoin() {
        return coinX >= 0;
    }

    public void reset() {
        previous = null;
        distance = Constants.INFINITY;
        visited = false;
        path = "";
    }

    public void markVisited() {
        visited = true;
    }

    public String getDescription() {
        String target = String.format("(%d,%d)", x, y);
        if (coinX < 0) {
            return target;
        }
        return target + String.format(" C(%d,%d)", coinX, coinY);
    }

    void transferArcs(Node source) {
        setUp(source.getUp());
        setDown(source.getDown());
        setRight(source.getRight());
        setLeft(source.getLeft());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y && coinX == node.coinX && coinY == node.coinY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, coinX, coinY);
    }
}
