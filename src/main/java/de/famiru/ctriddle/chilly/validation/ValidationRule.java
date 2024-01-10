package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

public interface ValidationRule {
    boolean isSatisfied(Matrix matrix, List<Integer> path);
}
