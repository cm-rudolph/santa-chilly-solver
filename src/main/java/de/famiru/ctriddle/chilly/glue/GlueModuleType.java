package de.famiru.ctriddle.chilly.glue;

public enum GlueModuleType {
    CONCORDE(false), OR_TOOLS(true);

    private final boolean atspSolver;

    GlueModuleType(boolean atspSolver) {
        this.atspSolver = atspSolver;
    }

    public boolean isAtspSolver() {
        return atspSolver;
    }
}
