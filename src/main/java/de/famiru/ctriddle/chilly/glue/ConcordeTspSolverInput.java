package de.famiru.ctriddle.chilly.glue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConcordeTspSolverInput implements TspSolverInput {
    private static final Logger LOGGER = LogManager.getLogger(ConcordeTspSolverInput.class);

    @Override
    public List<Integer> readSolution() {
        instructUser();

        if (!Files.isRegularFile(Path.of("chilly.sol"))) {
            LOGGER.error("File not found. Did you place chilly.sol in the working directory?");
            return List.of();
        }

        List<String> lines = readFile();

        Pattern numberPattern = Pattern.compile("[0-9]+");
        List<Integer> result = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = numberPattern.matcher(line);
            while (matcher.find()) {
                String number = matcher.group(0);
                result.add(Integer.parseInt(number));
            }
        }

        if (result.get(0) + 1 != result.size()) {
            LOGGER.warn("First number isn't the number of nodes.");
        } else {
            result.remove(0);
        }

        // close the cycle
        result.add(result.get(0));

        return result;
    }

    private void instructUser() {
        LOGGER.info("Place the solution as file chilly.sol into the working dir and press enter.");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> readFile() {
        try {
            return Files.readAllLines(Path.of("chilly.sol"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
