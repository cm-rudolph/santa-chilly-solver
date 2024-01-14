package de.famiru.ctriddle.chilly.tsp;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

public class DistanceToAtspTransformer {
    public Matrix transformDistanceMatrixToAtsp(Matrix matrix, List<List<Integer>> clusters) {
        Matrix result = new Matrix(matrix);
        for (int i = 0; i < matrix.getDimension(); i++) {
            for (int j = 0; j < matrix.getDimension(); j++) {
                int entry = result.getEntry(i, j);
                if (entry > 0) {
                    result.setEntry(i, j, entry + Constants.DISTANCE_TO_ATSP_WEIGHT);
                }
            }
        }
        for (List<Integer> cluster : clusters) {
            // move all outgoing arcs to preceding node in cluster cycle
            for (int i = 0; i < cluster.size() - 1; i++) {
                int nodeI1 = cluster.get(i);
                int nodeI2 = cluster.get((i + 1) % cluster.size());
                result.swapRows(nodeI1, nodeI2);
            }

            for (int i = 0; i < cluster.size(); i++) {
                int nodeI = cluster.get(i);
                for (int j = 0; j < cluster.size(); j++) {
                    int nodeJ = cluster.get(j);

                    if (i == j) {
                        // restore self connect zeroes
                        result.setPath(nodeI, nodeJ, "");
                        result.setEntry(nodeI, nodeJ, 0);
                    } else if (((i + 1) % cluster.size()) == j) {
                        // place an arc of zero weight to the next node
                        result.setPath(nodeI, nodeJ, "cluster shortcut");
                        result.setEntry(nodeI, nodeJ, 0);
                    } else {
                        // never use other connections within cluster
                        result.setPath(nodeI, nodeJ, "cluster disconnect");
                        result.setEntry(nodeI, nodeJ, Constants.INFINITY);
                    }
                }
            }
        }
        return result;
    }
}
