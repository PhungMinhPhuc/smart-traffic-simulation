package generator;

public class IdGenerator {
    public static String roadId(int index) {
        return String.format("R%02d", index);
    }

    public static String fowawrdWayId(String roadId) {
        return String.format("%s-FW", roadId);
    }

    public static String reverseWayId(String roadId) {
        return String.format("%s-RW", roadId);
    }

    public static String LaneId(String wayId, int index) {
        return String.format("%s-L%02d",  wayId, index);
    }

    public static String NodeId(int index) {
        return String.format("N%02d", index);
    }

    public static String PathId(String NodeId, int index) {
        return String.format("%s-P%02d", NodeId, index);
    }
}
