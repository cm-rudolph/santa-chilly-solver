package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.ArrayList;
import java.util.List;

public class ExitToStartOnlyOnceRule implements ValidationRule {
    @Override
    public boolean isSatisfied(Matrix matrix, List<Integer> path) {
        List<String> pathDescriptions = extractPathDescriptions(matrix, path);
        int count = 0;
        for (String description : pathDescriptions) {
            if (Constants.EXIT_TO_START_PATH.equals(description)) {
                count++;
            }
        }
        return count == 1;
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
}
