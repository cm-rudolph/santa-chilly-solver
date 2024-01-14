package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

class FirstNodeIsStartNodeRule implements ValidationRule {
    @Override
    public boolean isSatisfied(Matrix matrix, List<Integer> path) {
        return (path.get(0) % matrix.getDimension()) == 0;
    }
}
