package generator;

public class IdGenerator {
    private static int nodeCounter = 0;
    private static int roadCounter = 0;
    private static int vehicleCounter = 0;

    public static String nodeId() {
        return String.format("N%02d", ++nodeCounter);
    }

    public static String roadId() {
        return String.format("R%02d", ++roadCounter);
    }

    public static String pathId(String nodeId, int pathIndex) {
        return String.format("%s-P%03d", nodeId, pathIndex);
    }
    
    public static String vehicleId(String type) {
        return String.format("%s-%03d", type, ++vehicleCounter);
    }
}