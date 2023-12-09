package de.famiru.ctriddle.chilly.layer2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionParser {
    private static final Logger LOGGER = LogManager.getLogger(SolutionParser.class);

    public List<Integer> parseSolution(String fileName) {
        List<String> lines = readFile(fileName);

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

    private List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
