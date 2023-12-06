package de.famiru.ctriddle.chilly;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

public class Board {
    // 0: empty field, 1: obstacle, 2: coin, 3: hole, 4: exit
    private final FieldValue[][] field;
    private final Map<Coordinates, Coordinates> wormholes;
    private final int width;
    private final int height;
    @Getter
    private final Coordinates exit;

    Board(FieldValue[][] field, Map<Coordinates, Coordinates> wormholes) {
        this.field = field;
        this.width = field.length;
        this.height = field[0].length;
        this.wormholes = wormholes;
        this.exit = findExit();
    }

    public boolean canMoveUp(int playerX, int playerY) {
        return canMoveTo(playerX, playerY - 1);
    }

    public boolean canMoveDown(int playerX, int playerY) {
        return canMoveTo(playerX, playerY + 1);
    }

    public boolean canMoveLeft(int playerX, int playerY) {
        return canMoveTo(playerX - 1, playerY);
    }

    public boolean canMoveRight(int playerX, int playerY) {
        return canMoveTo(playerX + 1, playerY);
    }

    private boolean canMoveTo(int x, int y) {
        return getAt(x, y) != FieldValue.OBSTACLE;
    }

    public Coordinates moveUp(int playerX, int playerY) {
        int y = playerY + height;
        while (canMoveUp(playerX, y)) {
            y--;
            if (isWormholeAt(playerX, y)) {
                return getWormholeTarget(playerX, y);
            }
        }
        return new Coordinates(playerX, y % height);
    }

    public Coordinates moveDown(int playerX, int playerY) {
        int y = playerY;
        while (canMoveDown(playerX, y)) {
            y++;
            if (isWormholeAt(playerX, y)) {
                return getWormholeTarget(playerX, y);
            }
        }
        return new Coordinates(playerX, y % height);
    }

    public Coordinates moveLeft(int playerX, int playerY) {
        int x = playerX + width;
        while (canMoveLeft(x, playerY)) {
            x--;
            if (isWormholeAt(x, playerY)) {
                return getWormholeTarget(x, playerY);
            }
        }
        return new Coordinates(x % width, playerY);
    }

    public Coordinates moveRight(int playerX, int playerY) {
        int x = playerX;
        while (canMoveRight(x, playerY)) {
            x++;
            if (isWormholeAt(x, playerY)) {
                return getWormholeTarget(x, playerY);
            }
        }
        return new Coordinates(x % width, playerY);
    }

    private boolean isWormholeAt(int x, int y) {
        return wormholes.containsKey(new Coordinates(x % width, y % height));
    }

    private Coordinates getWormholeTarget(int x, int y) {
        return wormholes.get(new Coordinates(x % width, y % height));
    }

    public Optional<Coordinates> coinAtMoveUp(int playerX, int playerY) {
        int y = playerY + height;
        while (canMoveUp(playerX, y)) {
            y--;
            FieldValue value = getAt(playerX, y);
            if (value == FieldValue.COIN) {
                return Optional.of(new Coordinates(playerX, y % height));
            } else if (value == FieldValue.HOLE || value == FieldValue.EXIT) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<Coordinates> coinAtMoveDown(int playerX, int playerY) {
        int y = playerY;
        while (canMoveDown(playerX, y)) {
            y++;
            FieldValue value = getAt(playerX, y);
            if (value == FieldValue.COIN) {
                return Optional.of(new Coordinates(playerX, y % height));
            } else if (value == FieldValue.HOLE || value == FieldValue.EXIT) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<Coordinates> coinAtMoveLeft(int playerX, int playerY) {
        int x = playerX + width;
        while (canMoveLeft(x, playerY)) {
            x--;
            FieldValue value = getAt(x, playerY);
            if (value == FieldValue.COIN) {
                return Optional.of(new Coordinates(x % width, playerY));
            } else if (value == FieldValue.HOLE || value == FieldValue.EXIT) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<Coordinates> coinAtMoveRight(int playerX, int playerY) {
        int x = playerX;
        while (canMoveRight(x, playerY)) {
            x++;
            FieldValue value = getAt(x, playerY);
            if (value == FieldValue.COIN) {
                return Optional.of(new Coordinates(x % width, playerY));
            } else if (value == FieldValue.HOLE || value == FieldValue.EXIT) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public boolean isExit(int x, int y) {
        return getAt(x, y) == FieldValue.EXIT;
    }

    private FieldValue getAt(int x, int y) {
        return field[(x + width) % width][(y + height) % height];
    }

    private Coordinates findExit() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isExit(x, y)) {
                    return new Coordinates(x, y);
                }
            }
        }
        throw new IllegalArgumentException("The board doesn't have any exit.");
    }
}
