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
    public static final double LANE_CHANGE_DISTANCE = 60.0; // Real-life like distance for a lane change

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
    public static final double HOUSING_HEIGHT = 70;
    public static final double LIGHT_RADIUS = 7;
    public static final double SPACING = 15;

    // TRAFFIC LIGHT TIMING
    public static final double GREEN_DURATION = 20.0; // Seconds
    public static final double YELLOW_DURATION = 3.0;
    public static final double RED_DURATION = 20.0;

    // VEHICLE CONFIG
    public static final double SAFE_DISTANCE = 100.0;
    public static final double MUST_STOP_DISTANCE = 40.0;

    public static final double NORMAL_SPEED_RATIO = 0.8;
    public static final double NORMAL_ACCELERATION = 80.0;
    public static final double NORMAL_BRAKING = -50.0;

    public static final double AGGRESSIVE_SPEED_RATIO = 1.2;
    public static final double AGGRESSIVE_ACCELERATION = 120.0;
    public static final double AGGRESSIVE_BRAKING = -100.0;

    public static final double CAUTIOUS_SPEED_RATIO = 0.6;
    public static final double CAUTIOUS_ACCELERATION = 40.0;
    public static final double CAUTIOUS_BRAKING = -30.0;

    public static final double EMERGENCY_SPEED_RATIO = 1.0;
    public static final double EMERGENCY_ACCELERATION = 150.0;
    public static final double EMERGENCY_BRAKING = -120.0;

    // VEHICLE SPECIFICS
    
    // Car
    public static final double CAR_WIDTH = 12.0;
    public static final double CAR_LENGTH = 20.0;
    public static final double CAR_MAX_SPEED = 100.0;
    public static final String CAR_SOUND = "CarSound.wav";
    public static final Color CAR_COLOR = Color.BLUE;

    // Ambulance
    public static final double AMBULANCE_WIDTH = 12.0;
    public static final double AMBULANCE_LENGTH = 25.0;
    public static final double AMBULANCE_MAX_SPEED = 120.0;
    public static final String AMBULANCE_SOUND = "AmbulanceSirens.wav";
    public static final Color AMBULANCE_COLOR = Color.RED;
    
    // Bicycle
    public static final double BICYCLE_WIDTH = 5.0;
    public static final double BICYCLE_LENGTH = 8.0;
    public static final double BICYCLE_MAX_SPEED = 20.0;
    public static final String BICYCLE_SOUND = "Bell.wav";
    public static final Color BICYCLE_COLOR = Color.YELLOW;
    
    // Bus
    public static final double BUS_WIDTH = 12.0;
    public static final double BUS_LENGTH = 40.0;
    public static final double BUS_MAX_SPEED = 70.0;
    public static final String BUS_SOUND = "BusSound.wav";
    public static final Color BUS_COLOR = Color.GREEN;
    
    // FireTruck
    public static final double FIRE_TRUCK_WIDTH = 12.0;
    public static final double FIRE_TRUCK_LENGTH = 50.0;
    public static final double FIRE_TRUCK_MAX_SPEED = 110.0;
    public static final String FIRE_TRUCK_SOUND = "FireTruckSiren.wav";
    public static final Color FIRETRUCK_COLOR = Color.ORANGE;
    
    // Motorbike
    public static final double MOTORBIKE_WIDTH = 8.0;
    public static final double MOTORBIKE_LENGTH = 10.0;
    public static final double MOTORBIKE_MAX_SPEED = 80.0;
    public static final String MOTORBIKE_SOUND = "MotorbikeSound.wav";
    public static final Color MOTORBIKE_COLOR = Color.PURPLE;
    
    // Truck
    public static final double TRUCK_WIDTH = 12.0;
    public static final double TRUCK_LENGTH = 40.0;
    public static final double TRUCK_MAX_SPEED = 60.0;
    public static final String TRUCK_SOUND = "TruckSound.wav";
    public static final Color TRUCK_COLOR = Color.BLACK;

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