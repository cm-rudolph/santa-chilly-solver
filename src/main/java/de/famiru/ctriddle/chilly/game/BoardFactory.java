package de.famiru.ctriddle.chilly.game;

import java.util.List;

/**
 * This class is the entrypoint for creating an instance of a {@linkplain Board} including the initial player position.
 */
public class BoardFactory {
    private final LevelValidityChecker levelValidityChecker;
    private final LevelParser levelParser;
    private final LevelFileReader levelFileReader;

    public BoardFactory() {
        levelValidityChecker = new LevelValidityChecker();
        levelParser = new LevelParser();
        levelFileReader = new LevelFileReader();
    }

    public BoardAndPlayer loadLevel(String filename) {
        LevelFileReader.RowsAndWormholes rowsAndWormholes = levelFileReader.read(filename);
        return loadLevel(rowsAndWormholes.rows(), rowsAndWormholes.wormholes());
    }

    private BoardAndPlayer loadLevel(List<String> rows, String wormholes) {
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
