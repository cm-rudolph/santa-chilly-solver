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

import java.util.Collections;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final GlueModuleType GLUE_MODULE_TYPE = GlueModuleType.CONCORDE;

    private final BoardFactory boardFactory;
    private final DistanceToAtspTransformer distanceToAtspTransformer;
    private final AtspToTspTransformer atspToTspTransformer;
    private final SolutionValidator validator;
    private final GlueModuleFactory glueModuleFactory;

    public Main(BoardFactory boardFactory, DistanceToAtspTransformer distanceToAtspTransformer,
                AtspToTspTransformer atspToTspTransformer, GlueModuleFactory glueModuleFactory,
                SolutionValidator validator) {
        this.boardFactory = boardFactory;
        this.distanceToAtspTransformer = distanceToAtspTransformer;
        this.atspToTspTransformer = atspToTspTransformer;
        this.glueModuleFactory = glueModuleFactory;
        this.validator = validator;
    }

    public static void main(String[] args) {
        BoardFactory boardFactory = new BoardFactory();
        DistanceToAtspTransformer distanceToAtspTransformer = new DistanceToAtspTransformer();
        AtspToTspTransformer atspToTspTransformer = new AtspToTspTransformer();
        GlueModuleFactory glueModuleFactory = new GlueModuleFactory();
        SolutionValidator validator = new SolutionValidator();
        new Main(boardFactory, distanceToAtspTransformer, atspToTspTransformer, glueModuleFactory, validator).run();
    }

    private void run() {
        BoardFactory.BoardAndPlayer boardAndPlayer = boardFactory.loadLevel("level.txt");

        DijkstraSolver dijkstraSolver =
                new DijkstraSolver(boardAndPlayer.board(), boardAndPlayer.playerX(), boardAndPlayer.playerY());
        Matrix matrix = dijkstraSolver.createDistanceMatrix();

        List<List<Integer>> clusters = dijkstraSolver.getClusters();
        Matrix atspMatrix = distanceToAtspTransformer.transformDistanceMatrixToAtsp(matrix, clusters);

        Matrix transformedMatrix = GLUE_MODULE_TYPE.isAtspSolver() ?
                atspMatrix :
                atspToTspTransformer.transformAtspToTsp(atspMatrix);

        glueModuleFactory.createTspSolverOutput(GLUE_MODULE_TYPE).output(transformedMatrix);

        List<Integer> path = glueModuleFactory.createTspSolverInput(GLUE_MODULE_TYPE).readSolution();

        if (validator.isReverseSolution(GLUE_MODULE_TYPE.isAtspSolver(), atspMatrix, path)) {
            // possibly the solution is simply the wrong way around
            Collections.reverse(path);
        }

        if (!validator.isValidSolution(GLUE_MODULE_TYPE.isAtspSolver(), atspMatrix, path)) {

            //Collections.reverse(path);
            String instructions = createInstructions(path, atspMatrix);
            LOGGER.error("The solution is not valid. {}", instructions);
            return;
        }

        String instruction = createInstructions(path, atspMatrix);

        LOGGER.info("Solution (length {}): {}", instruction.length(), instruction);
    }

    private String createInstructions(List<Integer> path, Matrix matrix) {
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
        return sb.toString();
    }
}
