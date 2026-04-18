package baseFeature.vehicle;

import java.awt.Point;
import baseFeature.generator.BehaviorFactory;
import baseFeature.supportClass.SoundPlayer;

public class Firetruck extends Vehicle {

    public Firetruck(Point coordinate) {
        super(coordinate, 3.0, 6.0, 100.0, "firetruck_siren.wav", 
              BehaviorFactory.getBehavior(BehaviorFactory.BehaviorType.EMERGENCY));
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(this.getSound());
    }
}