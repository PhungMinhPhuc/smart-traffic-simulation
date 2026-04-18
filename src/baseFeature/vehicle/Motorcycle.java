package baseFeature.vehicle;

import java.awt.Point;
import baseFeature.generator.BehaviorFactory;
import baseFeature.supportClass.SoundPlayer;

public class Motorcycle extends Vehicle {

    public Motorcycle(Point coordinate) {
        super(coordinate, 1.0, 2.0, 80.0, "motor_sound.wav", BehaviorFactory.getRandomBehavior());
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(this.getSound());
    }
}