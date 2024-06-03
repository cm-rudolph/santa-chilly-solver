package de.famiru.ctriddle.chilly.glue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

class ConcordeDetector {
    private static final Logger LOGGER = LogManager.getLogger(ConcordeDetector.class);

    private static final boolean CONCORDE_PRESENT;

    static {
        boolean concordePresent = false;
        try {
            Process process = new ProcessBuilder("concorde").start();
            process.waitFor();
            concordePresent = true;
        } catch (IOException | InterruptedException e) {
            // ignore
        }
        CONCORDE_PRESENT = concordePresent;
        LOGGER.debug("Concorde detected: {}", concordePresent);
    }

    static boolean isConcordePresent() {
        return CONCORDE_PRESENT;
    }
}
