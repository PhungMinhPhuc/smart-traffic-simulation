package config;

public final class Constants {
    private Constants() {}

    // NODE CONFIG

    public static final double NODE_RADIUS = 100.0;

    // LANE CONFIG
    public static final double LANE_WIDTH = 30.0;
    public static final int LANES_PER_WAY = 2;

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
}
