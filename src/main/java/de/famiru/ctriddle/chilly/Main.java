package de.famiru.ctriddle.chilly;

import de.famiru.ctriddle.chilly.layer1.DijkstraSolver;
import de.famiru.ctriddle.chilly.layer2.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        String wormholes;
        List<String> rows;
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("level.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            wormholes = reader.readLine();
            rows = new ArrayList<>(40);
            while (reader.ready()) {
                rows.add(reader.readLine().replace("|", ""));
            }
        }

        BoardFactory.BoardAndPlayer boardAndPlayer = new BoardFactory().loadLevel(rows, wormholes);

        DijkstraSolver dijkstraSolver =
                new DijkstraSolver(boardAndPlayer.board(), boardAndPlayer.playerX(), boardAndPlayer.playerY());
        Matrix matrix = dijkstraSolver.createMatrix();
        LOGGER.info("{}", matrix.getDimension());
    }
}
