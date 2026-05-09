package model.vehicle;

import generator.BehaviorGenerator;
import model.map.Point;
import sound.SoundPlayer;

public class Ambulance extends Vehicle {
    private static final String DEFAULT_SOUND = "AmbulanceSound.wav";
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_LENGTH = 45.0;
    private static final double DEFAULT_MAX_SPEED = 120.0;

        public Ambulance(Point position) {
        super("Ambulance", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, position);
        this.setBehavior(BehaviorGenerator.getRandomBehavior());
    }

    @Override
    public String toString() {
        return "Ambulance [" + behavior.getBehaviorName() + "]";
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(DEFAULT_SOUND);
    }
}