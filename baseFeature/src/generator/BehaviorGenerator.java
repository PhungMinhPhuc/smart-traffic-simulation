package generator;

import model.vehicle.behavior.*;
import java.util.Random;

public class BehaviorGenerator {
    
    public enum BehaviorType {
        NORMAL, EMERGENCY, AGGRESSIVE
    }

    static final Random Rand = new Random();

    public static DriverBehavior getBehavior(BehaviorType type) {
        switch (type) {
            case EMERGENCY:
                return new EmergencyDriver();
            case AGGRESSIVE:
                return new AggressiveDriver();
            default:
                return new NormalDriver();
        }
    }

    public static DriverBehavior getRandomBehavior() {
        BehaviorType[] types = BehaviorType.values();
        // Chọn ngẫu nhiên index từ 0 đến độ dài của mảng enum
        BehaviorType randomType = types[Rand.nextInt(types.length)];
        return getBehavior(randomType);
    }
}