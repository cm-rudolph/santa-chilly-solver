package de.famiru.ctriddle.chilly.validation;

import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

public class SolutionValidator {
    private final List<ValidationRule> validationRules;

    public SolutionValidator() {
        validationRules = List.of(new ExitToStartOnlyOnceRule(),
                new NoDisconnectsRule(),
                new FirstNodeIsStartNodeRule(),
                new ClustersVisitedExactlyOnceRule(),
                new NodeAndGhostNodeVisitedAlternatelyRule());
    }

    public boolean isValidSolution(Matrix matrix, List<Integer> path) {
        return validationRules.stream()
                .allMatch(rule -> rule.isSatisfied(matrix, path));
    }
}
