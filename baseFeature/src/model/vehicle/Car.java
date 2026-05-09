package model.vehicle;

import generator.BehaviorGenerator;
import model.map.Point;
import sound.SoundPlayer;

public class Car extends Vehicle {
    private static final String DEFAULT_SOUND = "CarSound.wav";
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_LENGTH = 40.0;
    private static final double DEFAULT_MAX_SPEED = 100.0;

        public Car(Point position) {
        super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getRandomBehavior());
    }

    @Override
    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}