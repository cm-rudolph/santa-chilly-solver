package de.famiru.ctriddle.chilly.layer1;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Node {
    private final int x;
    private final int y;
    private final int coinX;
    private final int coinY;
    private Node up;
    private Node right;
    private Node down;
    private Node left;

    public Node(int x, int y) {
        this(x, y, -1, -1);
    }

    public Node(int x, int y, int coinX, int coinY) {
        this.x = x;
        this.y = y;
        this.coinX = coinX;
        this.coinY = coinY;
    }

    public boolean hasCoin() {
        return coinX >= 0;
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
