package model.vehicle;

import model.map.Point;
import generator.*;
import sound.SoundPlayer;

public class Truck extends Vehicle {
    private static final String DEFAULT_SOUND = "TruckSound.wav";
    private static final double DEFAULT_WIDTH = 25.0;
    private static final double DEFAULT_LENGTH = 55.0;
    private static final double DEFAULT_MAX_SPEED = 50.0;

    public Truck(Point position) {
        super("Truck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getRandomBehavior());
    }

    @Override
    public String toString() {
        return "Truck [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}