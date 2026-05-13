package config;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {
    }

    // LANE CONFIG
    public static final double LANE_WIDTH = 15.0;
    public static final Color LANE_COLOR = Color.GRAY;
    public static final int DEFAULT_LANE_COUNT = 3;
    public static final int MAX_LANE_COUNT = 10;
    public static final int MIN_LANE_COUNT = 1;

    // LANE MARKING CONFIG
    public static final double LANE_MARKING_WIDTH = 1.0;
    public static final Color LANE_MARKING_COLOR = Color.WHITE;

    // ROAD MARKING CONFIG
    public static final double ROAD_MARKING_WIDTH = 1.0;
    public static final Color ROAD_MARKING_COLOR = Color.WHITE;
    public static final double ROAD_MARKING_OFFSET = 30.0;

    // JUNCTION CONFIG
    public static final double JUNCTION_MIN_RADIUS = 60.0; // Minimum radius when no roads connected
    public static final Color JUNCTION_COLOR = Color.GRAY;
    public static final Color JUNCTION_CENTER_POINT_COLOR = Color.WHITE;

    // STOP LINE CONFIG
    public static final double STOP_LINE_WIDTH = 2.0;
    public static final Color STOP_LINE_MARKING_COLOR = Color.WHITE;

    // PATH CONFIG
    public static final double PATH_WIDTH = 2.0;
    public static final Color PATH_COLOR = Color.RED;

    // GEOMETRY / MATH
    public static final double EPS = 1e-9;

    // TRAFFIC LIGHT CONFIG
    public static final double HOUSING_WIDTH = 18;
    public static final double HOUSING_HEIGHT = 60;
    public static final double LIGHT_RADIUS = 7;
    public static final double SPACING = 15;

    // TRAFFIC LIGHT TIMING
    public static final int GREEN_TIME = 30;
    public static final int YELLOW_TIME = 3;
    public static final int RED_TIME = 30;

    public static final double GREEN_DURATION = 30.0; // Seconds
    public static final double YELLOW_DURATION = 3.0;
    public static final double RED_DURATION = 30.0;

    // VEHICLE CONFIG

    // CAR CONFIG
    public static final double CAR_WIDTH = 30.0;
    public static final double CAR_LENGTH = 15.0;
    public static final String CAR_SOUND = "CarSound.wav";
    public static final Color CAR_COLOR = Color.BLUE;
    public static final double CAR_MAX_SPEED = 100.0;

    // SIMULATION CONFIG
    public static final int TICK_RATE_MS = 1000;
    public static final double NANOS_PER_SECOND = 1e9; // 1 sec = 1e9 nano sec

    // GUI CONFIG
    public static final double MIN_DISTANCE_TO_END_POINT = 5.0;

    // ZOOM CONFIG
    public static final double MIN_ZOOM = 0.1;
    public static final double MAX_ZOOM = 10000.0;
    public static final double ZOOM_STEP = 0.1;
}