package generator;

public class IdGenerator {
    public static String roadId(int index) {
        return String.format("R%02d", index);
    }

    public static String nodeId(int index) {
        return String.format("N%02d", index);
    }

    public static String pathId(String NodeId, int index) {
        return String.format("%s-P%02d", NodeId, index);
    }
}
