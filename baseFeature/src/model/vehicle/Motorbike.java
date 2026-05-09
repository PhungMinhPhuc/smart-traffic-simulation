package model.vehicle;

import generator.BehaviorGenerator;
import model.map.Point;
import sound.SoundPlayer;

public class Motorbike extends Vehicle {
    private static final String DEFAULT_SOUND = "MotorbikeSound.wav";
    private static final double DEFAULT_WIDTH = 10.0;
    private static final double DEFAULT_LENGTH = 20.0;
    private static final double DEFAULT_MAX_SPEED = 90.0;

        public Motorbike(Point position) {
        super("Motorbike", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getRandomBehavior());
    }

    @Override
    public String toString() {
        return "Motorbike [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}