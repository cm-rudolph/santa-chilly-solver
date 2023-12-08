package de.famiru.ctriddle.chilly.layer2;

import de.famiru.ctriddle.chilly.Constants;

public class AtspToTspTransformer {
    Matrix transform(Matrix atspMatrix) {
        int dimension = atspMatrix.getDimension();

        Matrix tspMatrix = new Matrix(dimension * 2);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tspMatrix.setEntry(i, j, Constants.INFINITY);
                tspMatrix.setEntry(i + dimension, j + dimension, Constants.INFINITY);

                if (i != j) {
                    tspMatrix.setEntry(i + dimension, j, atspMatrix.getEntry(i, j) + 200);
                    tspMatrix.setEntry(i, j + dimension, atspMatrix.getEntry(j, i) + 200);
                }
            }
        }

        return tspMatrix;
    }
}
