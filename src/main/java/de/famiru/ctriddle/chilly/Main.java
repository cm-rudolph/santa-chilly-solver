package de.famiru.ctriddle.chilly;

import de.famiru.ctriddle.chilly.distance.DijkstraSolver;
import de.famiru.ctriddle.chilly.game.BoardFactory;
import de.famiru.ctriddle.chilly.glue.GlueModuleFactory;
import de.famiru.ctriddle.chilly.glue.GlueModuleType;
import de.famiru.ctriddle.chilly.tsp.AtspToTspTransformer;
import de.famiru.ctriddle.chilly.tsp.DistanceToAtspTransformer;
import de.famiru.ctriddle.chilly.validation.SolutionValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final GlueModuleType GLUE_MODULE_TYPE = GlueModuleType.CONCORDE;

    public static void main(String[] args) {
        BoardFactory.BoardAndPlayer boardAndPlayer = new BoardFactory().loadLevel("level.txt");

        DijkstraSolver dijkstraSolver =
                new DijkstraSolver(boardAndPlayer.board(), boardAndPlayer.playerX(), boardAndPlayer.playerY());
        Matrix matrix = dijkstraSolver.createDistanceMatrix();

        Matrix atspMatrix = new DistanceToAtspTransformer()
                .transformDistanceMatrixToAtsp(matrix, dijkstraSolver.getClusters());

        Matrix tspMatrix = new AtspToTspTransformer().transformAtspToTsp(atspMatrix);

        GlueModuleFactory glueModuleFactory = new GlueModuleFactory();
        glueModuleFactory.createTspSolverOutput(GLUE_MODULE_TYPE).output(tspMatrix);

        if (!Files.isRegularFile(Path.of("chilly.sol"))) {
            LOGGER.error("File not found. Did you place chilly.sol in the working directory?");
            return;
        }

        List<Integer> path = glueModuleFactory.createTspSolverInput(GLUE_MODULE_TYPE).readSolution();

        SolutionValidator validator = new SolutionValidator();
        if (!validator.isValidSolution(atspMatrix, path)) {
            // possibly the solution is simply the wrong way around
            Collections.reverse(path);
        }
        if (!validator.isValidSolution(atspMatrix, path)) {
            LOGGER.error("The solution is not valid.");
            return;
        }

        StringBuilder sb = createInstructions(path, atspMatrix);
        LOGGER.info("Solution (length {}): {}", sb.length(), sb.toString());
    }

    private static StringBuilder createInstructions(List<Integer> path, Matrix matrix) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < path.size(); j++) {
            int i = path.get(j);
            if (j > 0) {
                String fragment = matrix.getPath(path.get(j - 1) % matrix.getDimension(), i % matrix.getDimension());
                if (!fragment.startsWith("cluster") && !fragment.equals(Constants.EXIT_TO_START_PATH)) {
                    sb.append(fragment);
                }
                LOGGER.debug("{}", fragment);
            }
            if (i >= matrix.getDimension()) {
                LOGGER.debug("{}# {}", i % matrix.getDimension(), matrix.getDescription(i % matrix.getDimension()));
            } else {
                LOGGER.debug("{}: {}", i, matrix.getDescription(i));
            }
        }
        return sb;
    }
}
