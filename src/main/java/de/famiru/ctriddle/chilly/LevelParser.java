package de.famiru.ctriddle.chilly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LevelParser {
    private static final Map<Character, FieldValue> CHARACTER_TO_FIELD_VALUE = Map.of(
            'P', FieldValue.EMPTY,
            ' ', FieldValue.EMPTY,
            '.', FieldValue.EMPTY,
            '#', FieldValue.OBSTACLE,
            'Y', FieldValue.OBSTACLE,
            'T', FieldValue.OBSTACLE,
            '$', FieldValue.COIN,
            'G', FieldValue.COIN,
            'O', FieldValue.HOLE,
            'X', FieldValue.EXIT);

    ParsingResult parse(List<String> rows, String wormholes) {
        FieldValue[][] field = new FieldValue[rows.get(0).length()][rows.size()];
        int playerX = -1;
        int playerY = -1;
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                field[x][y] = CHARACTER_TO_FIELD_VALUE.get(row.charAt(x));
                if (field[x][y] == null) {
                    throw new IllegalArgumentException("Unexpected character.");
                }
                if (row.charAt(x) == 'P') {
                    playerX = x;
                    playerY = y;
                }
            }
        }

        return new ParsingResult(field, parseWormholes(wormholes), playerX, playerY);
    }

    private Map<Coordinates, Coordinates> parseWormholes(String wormholes) {
        Map<Coordinates, Coordinates> result = new HashMap<>();
        Pattern pattern = Pattern.compile("\\(([1-9][0-9]*),([1-9][0-9]*)\\)->\\(([1-9][0-9]*),([1-9][0-9]*)\\);?");
        Matcher matcher = pattern.matcher(wormholes);
        while (matcher.find()) {
            Coordinates start = new Coordinates(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Coordinates target = new Coordinates(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            result.put(start, target);
        }
        return Map.copyOf(result);
    }

    record ParsingResult(FieldValue[][] field, Map<Coordinates, Coordinates> wormholes, int playerX, int playerY) {
    }
}
