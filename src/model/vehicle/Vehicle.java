package model.vehicle;

public class Vehicle {
	private int id;
	private static int vehicleQty = 0;

	public Vehicle() {
		this.id = vehicleQty;
		vehicleQty++;
	}

	public int getId() {
		return id;
	}
}
