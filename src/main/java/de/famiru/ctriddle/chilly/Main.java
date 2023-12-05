package de.famiru.ctriddle.chilly;

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

        Solver solver = new Solver(boardAndPlayer.board(), boardAndPlayer.playerX(), boardAndPlayer.playerY());
        solver.createGraph();
    }
}
