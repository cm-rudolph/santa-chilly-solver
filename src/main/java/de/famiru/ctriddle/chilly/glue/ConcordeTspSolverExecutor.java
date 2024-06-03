package de.famiru.ctriddle.chilly.glue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

class ConcordeTspSolverExecutor implements TspSolverExecutor {
    private static final Logger LOGGER = LogManager.getLogger(ConcordeTspSolverExecutor.class);

    @Override
    public void execute() {
        deleteOldSolutionFile();

        if (!ConcordeDetector.isConcordePresent()) {
            return;
        }

        boolean success = false;
        try {
            Process process = new ProcessBuilder("concorde", "chilly.tsp").start();
            LOGGER.info("Executing concorde...");

            logOutput(process);

            int returnCode = process.waitFor();
            LOGGER.debug("Concorde returned exit code {}.", returnCode);
            success = returnCode == 0;
        } catch (IOException e) {
            LOGGER.error("Failed to execute concorde.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Interrupted during execution of concorde.", e);
        }

        if (!success) {
            LOGGER.warn("Didn't find chilly.sol in working directory. Falling back to manual mode.");
            LOGGER.info("Please pass chilly.tsp to a solver able to handle files in TSPLIB format.");
        }
    }

    private void deleteOldSolutionFile() {
        try {
            Files.delete(Path.of("chilly.sol"));
        } catch (IOException ignored) {
        }
    }

    private void logOutput(Process process) throws IOException {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                LOGGER.debug("Concorde output:");
                LOGGER.debug("================");
                String line;
                while ((line = reader.readLine()) != null) {
                    LOGGER.debug("{}", line);
                }
                LOGGER.debug("================");
            } catch (IOException e) {
                LOGGER.error("Failed to process concorde output.", e);
            }
        }).start();
    }
}
