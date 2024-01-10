package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

public class NoDisconnectsRule implements ValidationRule {
    @Override
    public boolean isSatisfied(Matrix matrix, List<Integer> path) {
        for (int j = 0; j < path.size(); j++) {
            int i = path.get(j);

            if (j > 0 && matrix.getEntry(path.get(j - 1) % matrix.getDimension(), i % matrix.getDimension()) >= Constants.INFINITY) {
                return false;
            }
        }

        return true;
    }
}
