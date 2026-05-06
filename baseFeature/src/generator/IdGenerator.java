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

    public static String forwardWayId(String roadId) {
        return String.format("%s-FW", roadId);
    }

    public static String reverseWayId(String roadId) {
        return String.format("%s-RW", roadId);
    }

    public static String laneId(String wayId, int laneIndex) {
        return String.format("%s-L%02d", wayId, laneIndex);
    }

    public static String pathId(String nodeId, int pathIndex) {
        return String.format("%s-P%02d", nodeId, pathIndex);
    }
    
    public static String vehicleId(String type) {
        // Ví dụ: Car-01, Ambu-02
        return String.format("%s-%03d", type, ++vehicleCounter);
    }
}