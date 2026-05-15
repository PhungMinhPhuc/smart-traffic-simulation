package render;

import javafx.scene.Group;
import model.vehicle.Vehicle;

public interface IVehicleRenderer extends IRender<Vehicle> {
    Group createNode(Vehicle vehicle);
    void updateNode(Group node, Vehicle vehicle);
}
