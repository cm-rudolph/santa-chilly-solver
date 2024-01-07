package de.famiru.ctriddle.chilly.tsp;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.ArrayList;
import java.util.List;

public class SolutionValidator {
    public boolean isValidSolution(Matrix matrix, List<Integer> path) {
        List<String> paths = extractPaths(matrix, path);
        return containsExitToStartExactlyOnce(paths)
                && containsNoDisconnects(matrix, path)
                && beginsWithStartNode(matrix, path);
    }

    private boolean beginsWithStartNode(Matrix matrix, List<Integer> path) {
        return (path.get(0) % matrix.getDimension()) == 0;
    }

    private List<String> extractPaths(Matrix matrix, List<Integer> path) {
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

    private boolean containsExitToStartExactlyOnce(List<String> paths) {
        int count = 0;
        for (String path : paths) {
            if (Constants.EXIT_TO_START_PATH.equals(path)) {
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
}
