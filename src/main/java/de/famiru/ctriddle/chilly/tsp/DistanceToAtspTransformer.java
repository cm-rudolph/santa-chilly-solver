package de.famiru.ctriddle.chilly.tsp;

import de.famiru.ctriddle.chilly.Constants;
import de.famiru.ctriddle.chilly.Matrix;

import java.util.List;

public class DistanceToAtspTransformer {
    public void transformDistanceMatrixToAtsp(Matrix matrix, List<List<Integer>> clusters) {
        for (List<Integer> cluster : clusters) {
            // move all outgoing arcs to preceding node in cluster cycle
            for (int i = 0; i < cluster.size() - 1; i++) {
                int nodeI1 = cluster.get(i);
                int nodeI2 = cluster.get((i + 1) % cluster.size());
                matrix.swapRows(nodeI1, nodeI2);
            }

            for (int i = 0; i < cluster.size(); i++) {
                int nodeI = cluster.get(i);
                for (int j = 0; j < cluster.size(); j++) {
                    int nodeJ = cluster.get(j);

                    if (i == j) {
                        // restore self connect zeroes
                        matrix.setPath(nodeI, nodeJ, "");
                        matrix.setEntry(nodeI, nodeJ, 0);
                    } else if (((i + 1) % cluster.size()) == j) {
                        // place an arc of zero weight to the next node
                        matrix.setPath(nodeI, nodeJ, "cluster shortcut");
                        matrix.setEntry(nodeI, nodeJ, 0);
                    } else {
                        // never use other connections within cluster
                        matrix.setPath(nodeI, nodeJ, "cluster disconnect");
                        matrix.setEntry(nodeI, nodeJ, Constants.INFINITY);
                    }
                }
            }
        }
    }
}
