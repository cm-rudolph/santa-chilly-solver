package de.famiru.ctriddle.chilly.glue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StdInTspSolverInput implements TspSolverInput {
    private static final Logger LOGGER = LogManager.getLogger(StdInTspSolverInput.class);

    @Override
    public List<Integer> readSolution() {
        instructUser();

        Scanner scanner = new Scanner(System.in);
        String solution = scanner.nextLine();

        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher matcher = numberPattern.matcher(solution);
        List<Integer> result = new ArrayList<>();
        while (matcher.find()) {
            String number = matcher.group(0);
            result.add(Integer.parseInt(number));
        }

        // close the cycle
        if (!Objects.equals(result.get(0), result.get(result.size() - 1))) {
            result.add(result.get(0));
        }

        return result;
    }

    private void instructUser() {
        LOGGER.info("Please paste the solution in a single row and press enter:");
    }
}
