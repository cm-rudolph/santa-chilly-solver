package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

class NodeAndGhostNodeVisitedAlternatelyRule implements ValidationRule {
    @Override
    public boolean isSatisfied(Matrix matrix, List<Integer> path) {
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
}
