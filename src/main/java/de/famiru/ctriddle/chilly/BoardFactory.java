package de.famiru.ctriddle.chilly;

import java.util.List;

public class BoardFactory {
    private final LevelValidityChecker levelValidityChecker;
    private final LevelParser levelParser;

    public BoardFactory() {
        levelValidityChecker = new LevelValidityChecker();
        levelParser = new LevelParser();
    }

    public BoardAndPlayer loadLevel(List<String> rows, String wormholes) {
        if (!levelValidityChecker.isValid(rows)) {
            throw new IllegalArgumentException("This level is invalid.");
        }
        if (!levelValidityChecker.isValid(wormholes)) {
            throw new IllegalArgumentException("Invalid wormholes. Expected string like (1,2)->(3,4);(5,6)->(7,8).");
        }
        LevelParser.ParsingResult parsingResult = levelParser.parse(rows, wormholes);
        return new BoardAndPlayer(new Board(parsingResult.field(), parsingResult.wormholes()),
                parsingResult.playerX(), parsingResult.playerY());
    }

    public record BoardAndPlayer(Board board, int playerX, int playerY) {
    }
}
