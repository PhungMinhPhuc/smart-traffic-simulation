package vehicle;

import utility.TrafficPoint;
import utility.TrafficVector;

public class Vehicle {
    private TrafficPoint position;
    private TrafficVector direction;

    public Vehicle() {

    }

    public TrafficPoint getPosition() {
        return position;
    }

    public void setPosition(TrafficPoint position) {
        this.position = position.clone();
    }

    public void setDirection(TrafficVector direction) {
        this.direction = direction.clone();
    }

    public void setDirection(TrafficPoint startPoint,  TrafficPoint endPoint) {
        this.direction = new TrafficVector(startPoint, endPoint);
    }
}
