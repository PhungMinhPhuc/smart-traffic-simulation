package config;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {}

    // LANE CONFIG
    public static final double LANE_WIDTH = 15.0;
    public static final Color LANE_COLOR = Color.GRAY;
    public static final int DEFAULT_LANE_COUNT = 2;

    // LANE MARKING CONFIG
    public static final double LANE_MARKING_WIDTH = 1.0;
    public static final Color LANE_MARKING_COLOR = Color.WHITE;

    // ROAD MARKING CONFIG


    // JUNCTION CONFIG
    public static final double JUNCTION_RADIUS = 70.0;

    // STOP LINE CONFIG
    public static final double STOP_LINE_WIDTH = 2.0;
    public static final Color STOP_LINE_MARKING_COLOR = Color.WHITE;

    // PATH CONFIG
    public static final double PATH_WIDTH = 2.0;
    public static final Color PATH_COLOR = Color.RED;

    // GEOMETRY / MATH
    public static final double EPS = 1e-9;

    // TRAFFIC LIGHT TIMING
    public static final int GREEN_TIME = 30;
    public static final int YELLOW_TIME = 3;
    public static final int RED_TIME = 30;

    public static final double GREEN_DURATION = 30.0; // Seconds
    public static final double YELLOW_DURATION = 3.0;
    public static final double RED_DURATION = 30.0;

    // VEHICLE CONFIG
    public static final double MAX_SPEED = 60.0;
    public static final double ACCELERATION = 2.0;

    // SIMULATION CONFIG
    public static final int TICK_RATE_MS = 1000;

    // GUI CONFIG
    public static final double MIN_DISTANCE_TO_END_POINT = 5.0;
}