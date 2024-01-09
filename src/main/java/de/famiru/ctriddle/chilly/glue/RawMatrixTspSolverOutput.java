package de.famiru.ctriddle.chilly.glue;

import de.famiru.ctriddle.chilly.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;

class RawMatrixTspSolverOutput extends FileBasedTspSolverOutput {
    private static final Logger LOGGER = LogManager.getLogger(RawMatrixTspSolverOutput.class);

    RawMatrixTspSolverOutput() {
        super("dist.txt");
    }

    @Override
    public void writeData(PrintWriter writer, Matrix matrix) {
        for (int i = 0; i < matrix.getDimension(); i++) {
            for (int j = 0; j < matrix.getDimension(); j++) {
                if (j > 0) {
                    writer.print(' ');
                }
                writer.print(matrix.getEntry(i, j));
            }
            if (i + 1 < matrix.getDimension()) {
                writer.println();
            }
        }
    }

    @Override
    protected void instructUser() {
        LOGGER.info("Please pass data.txt to a solver able to handle a raw TSP matrix file.");
    }
}
