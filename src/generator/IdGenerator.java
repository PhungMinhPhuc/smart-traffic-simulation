package generator;

public class IdGenerator {
    public static int roadIdCounter = 0;
    public static int nodeIdCounter = 0;
    private static int vehicleCounter = 0;

    public static String roadId() {
        return String.format("R%02d", roadIdCounter++);
    }

    public static String nodeId() {
        return String.format("N%02d", nodeIdCounter++);
    }

    public static String pathId(String nodeId, int index) {
        return String.format("%s-P%02d", nodeId, index);
    }

    public static String vehicleId(String type) {
        return String.format("%s-%03d", type, ++vehicleCounter);
    }
}
