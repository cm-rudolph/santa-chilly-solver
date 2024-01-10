package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Matrix;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClustersVisitedExactlyOnceRule implements ValidationRule {
    public static final Pattern COIN_PATTERN = Pattern.compile(" C\\(([0-9]+),([0-9]+)\\)");

    @Override
    public boolean isSatisfied(Matrix matrix, List<Integer> path) {
        Coordinates previous = null;
        Set<Coordinates> visitedClusters = new HashSet<>();
        for (Integer i : path) {
            Matcher matcher = COIN_PATTERN.matcher(matrix.getDescription(i % matrix.getDimension()));
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                Coordinates coordinates = new Coordinates(x, y);

                if (visitedClusters.contains(coordinates)) {
                    if (!coordinates.equals(previous)) {
                        return false;
                    }
                }

                visitedClusters.add(coordinates);
                previous = coordinates;
            }
        }
        return true;
    }

    private record Coordinates(int x, int y) {
    }
}
