package de.famiru.ctriddle.chilly.tsp;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionValidator {
    public boolean isValidSolution(Matrix matrix, List<Integer> path) {
        return containsExitToStartExactlyOnce(matrix, path)
                && containsNoDisconnects(matrix, path)
                && beginsWithStartNode(matrix, path)
                && clustersVisitedExactlyOnce(matrix, path)
                && nodeAndGhostNodeVisitedAlternately(matrix, path);
    }

    private boolean beginsWithStartNode(Matrix matrix, List<Integer> path) {
        return (path.get(0) % matrix.getDimension()) == 0;
    }

    private List<String> extractPathDescriptions(Matrix matrix, List<Integer> path) {
        List<String> result = new ArrayList<>(path.size());

        for (int j = 0; j < path.size(); j++) {
            int i = path.get(j);
            if (j > 0) {
                String fragment = matrix.getPath(path.get(j - 1) % matrix.getDimension(), i % matrix.getDimension());
                result.add(fragment);
            }
        }

        return result;
    }

    private boolean containsExitToStartExactlyOnce(Matrix matrix, List<Integer> path) {
        List<String> pathDescriptions = extractPathDescriptions(matrix, path);
        int count = 0;
        for (String description : pathDescriptions) {
            if (Constants.EXIT_TO_START_PATH.equals(description)) {
                count++;
            }
        }
        return count == 1;
    }

    private boolean containsNoDisconnects(Matrix matrix, List<Integer> path) {
        for (int j = 0; j < path.size(); j++) {
            int i = path.get(j);

            if (j > 0
                    && matrix.getEntry(path.get(j - 1) % matrix.getDimension(), i % matrix.getDimension()) >= Constants.INFINITY) {
                return false;
            }
        }

        return true;
    }

    private boolean clustersVisitedExactlyOnce(Matrix matrix, List<Integer> path) {
        Coordinates previous = null;
        Pattern coinPattern = Pattern.compile(" C\\(([0-9]+),([0-9]+)\\)");
        Set<Coordinates> visitedClusters = new HashSet<>();
        for (Integer i : path) {
            Matcher matcher = coinPattern.matcher(matrix.getDescription(i % matrix.getDimension()));
            if (matcher.find()) {
                Coordinates coordinates =
                        new Coordinates(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
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

    private boolean nodeAndGhostNodeVisitedAlternately(Matrix matrix, List<Integer> path) {
        for (int i = 0; i < matrix.getDimension(); i++) {
            Integer first = path.get(i * 2);
            Integer second = path.get(i * 2 + 1);
            if (first >= matrix.getDimension() || second < matrix.getDimension()) {
                return false;
            }
            if (first != (second % matrix.getDimension())) {
                return false;
            }
        }
        return true;
    }

    private record Coordinates(int x, int y) {
    }
}
