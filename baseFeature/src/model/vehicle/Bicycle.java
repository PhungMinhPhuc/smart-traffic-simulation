package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import sound.SoundPlayer;

public class Bicycle extends Vehicle {
    private static final String DEFAULT_SOUND = "BicycleSound.wav";
    private static final double DEFAULT_WIDTH = 8.0;
    private static final double DEFAULT_LENGTH = 18.0;
    private static final double DEFAULT_MAX_SPEED = 25.0;

    public Bicycle(DriverBehavior behavior) {
        super("Bicycle", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
    }

    @Override
    public String toString() {
        return "Bicycle [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}