package model.vehicle;

import generator.BehaviorGenerator;
import model.map.Point;
import sound.SoundPlayer;

public class Bus extends Vehicle {
    private static final String DEFAULT_SOUND = "BusSound.wav";
    private static final double DEFAULT_WIDTH = 25.0;
    private static final double DEFAULT_LENGTH = 60.0;
    private static final double DEFAULT_MAX_SPEED = 60.0;

       public Bus(Point position) {
        super("Bus", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getRandomBehavior());
    }

    @Override
    public String toString() {
        return "Bus [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}