package de.famiru.ctriddle.chilly;

import de.famiru.ctriddle.chilly.layer1.DijkstraSolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
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

        DijkstraSolver dijkstraSolver = new DijkstraSolver(boardAndPlayer.board(), boardAndPlayer.playerX(), boardAndPlayer.playerY());
        dijkstraSolver.createGraph();
    }
}
