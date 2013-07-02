package us.sustainify.commute.domain.service.google.directions;

import com.google.api.client.util.Key;

public class TransitLine {
	@Key
	private String name;

	@Key
	private TransitVehicle vehicle;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TransitVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(TransitVehicle vehicle) {
		this.vehicle = vehicle;
	}

}
