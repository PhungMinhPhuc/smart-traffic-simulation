package model.vehicle;

import generator.BehaviorGenerator;
import model.map.Point;
import sound.SoundPlayer;

public class FireTruck extends Vehicle {
    private static final String DEFAULT_SOUND = "FireTruckSound.wav";
    private static final double DEFAULT_WIDTH = 25.0;
    private static final double DEFAULT_LENGTH = 55.0;
    private static final double DEFAULT_MAX_SPEED = 110.0;

        public FireTruck(Point position) {
        super("FireTruck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getBehavior(BehaviorGenerator.BehaviorType.EMERGENCY));
    }

    @Override
    public String toString() {
        return "FireTruck [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}