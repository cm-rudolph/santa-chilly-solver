package de.famiru.ctriddle.chilly.glue;

import de.famiru.ctriddle.chilly.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

class ConcordeTspSolverOutput extends FileBasedTspSolverOutput {
    private static final Logger LOGGER = LogManager.getLogger(ConcordeTspSolverOutput.class);

    ConcordeTspSolverOutput() {
        super("chilly.tsp");
    }

    @Override
    public void writeData(PrintWriter writer, Matrix matrix) {
        writer.println("NAME: chilly");
        writer.println("TYPE: TSP");
        writer.println("COMMENT: Help chilly collect all presents");
        writer.println("DIMENSION: " + matrix.getDimension());
        writer.println("EDGE_WEIGHT_TYPE: EXPLICIT");
        writer.println("EDGE_WEIGHT_FORMAT: FULL_MATRIX");
        writer.println("EDGE_WEIGHT_SECTION");

        for (int i = 0; i < matrix.getDimension(); i++) {
            for (int j = 0; j < matrix.getDimension(); j++) {
                writer.print(String.format(" %8d", matrix.getEntry(i, j)));
            }
            writer.println();
        }

        writer.println("EOF");
    }

    @Override
    protected void instructUser() {
        if (!ConcordeDetector.isConcordePresent()) {
            LOGGER.info("concorde has not been found in your PATH. Falling back to manual mode.");
            LOGGER.info("Please pass chilly.tsp to a solver able to handle files in TSPLIB format.");
        }
    }
}
