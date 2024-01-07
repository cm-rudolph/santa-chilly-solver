package de.famiru.ctriddle.chilly.tsp;

import de.famiru.ctriddle.chilly.Matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

public class TspFileWriter {
    public void writeTspFile(String fileName, Matrix atspMatrix) {
        Matrix matrix = new AtspToTspTransformer().transform(atspMatrix);

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
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
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
