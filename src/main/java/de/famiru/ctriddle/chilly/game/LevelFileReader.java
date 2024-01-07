package de.famiru.ctriddle.chilly.game;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class LevelFileReader {
    public RowsAndWormholes read(String name) {
        String wormholes;
        List<String> rows;
        try (InputStream is = new FileInputStream(name);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            wormholes = reader.readLine();
            rows = new ArrayList<>();
            while (reader.ready()) {
                rows.add(reader.readLine().replace("|", ""));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new RowsAndWormholes(rows, wormholes);
    }

    record RowsAndWormholes (List<String> rows, String wormholes) {
    }
}
