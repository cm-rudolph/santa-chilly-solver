package de.famiru.ctriddle.chilly;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

class LevelValidityChecker {
    private static final Set<Character> VALID_CHARS;
    private static final Pattern WORMHOLES_PATTERN =
            Pattern.compile("(\\([1-9][0-9]*,[1-9][0-9]*\\)->\\([1-9][0-9]*,[1-9][0-9]*\\);?)+");

    static {
        String validCharString = " .#YTXP$GO";
        Set<Character> validChars = new HashSet<>();
        for (int i = 0; i < validCharString.length(); i++) {
            validChars.add(validCharString.charAt(i));
        }
        VALID_CHARS = Set.copyOf(validChars);
    }

    boolean isValid(List<String> rows) {
        return isNonEmpty(rows) &&
                allNonNull(rows) &&
                isRectangular(rows) &&
                containsOnlyValidCharacters(rows) &&
                hasExactlyOnePlayer(rows);
    }

    boolean isValid(String wormholes) {
        return WORMHOLES_PATTERN.matcher(wormholes).matches();
    }

    private boolean isNonEmpty(List<String> rows) {
        return !rows.isEmpty();
    }

    private boolean allNonNull(List<String> rows) {
        for (String row : rows) {
            if (row == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isRectangular(List<String> rows) {
        int length = rows.get(0).length();
        for (String row : rows) {
            if (row.length() != length) {
                return false;
            }
        }
        return true;
    }

    private boolean containsOnlyValidCharacters(List<String> rows) {
        for (String row : rows) {
            for (int i = 0; i < row.length(); i++) {
                if (!VALID_CHARS.contains(row.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasExactlyOnePlayer(List<String> rows) {
        int numberOfPlayers = 0;
        for (String row : rows) {
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == 'P') {
                    numberOfPlayers++;
                }
            }
        }
        return numberOfPlayers == 1;
    }
}
