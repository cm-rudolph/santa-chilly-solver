package de.famiru.ctriddle.chilly;

public final class Constants {
    public static final String EXIT_TO_START_PATH = "exit to start";
    public static final int DISTANCE_TO_ATSP_WEIGHT = 200;
    public static final int ATSP_TO_STSP_WEIGHT = 2000;
    public static final int INFINITY = 99999999 - DISTANCE_TO_ATSP_WEIGHT - ATSP_TO_STSP_WEIGHT;

    private Constants() {
    }
}
