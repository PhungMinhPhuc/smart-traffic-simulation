package baseFeature.generator;

import java.util.Random;
import baseFeature.behavior.*;

public class BehaviorFactory {
    
    // Định nghĩa các kiểu hành vi có trong hệ thống
    public enum BehaviorType {
        NORMAL, EMERGENCY, AGGRESSIVE
    }

    private static final Random RANDOM = new Random();

    // Trả về đối tượng hành vi cụ thể dựa trên loại được yêu cầu.
    public static DriverBehavior getBehavior(BehaviorType type) {
        switch (type) {
            case EMERGENCY:
                return new EmergencyDriver();
            case AGGRESSIVE:
                return new AggressiveDriver();
            case NORMAL:
            default:
                return new NormalDriver();
        }
    }

    //Lấy ngẫu nhiên một hành vi lái xe.
    public static DriverBehavior getRandomBehavior() {
        BehaviorType[] types = BehaviorType.values();
        BehaviorType randomType = types[RANDOM.nextInt(types.length)];
        return getBehavior(randomType);
    }
}