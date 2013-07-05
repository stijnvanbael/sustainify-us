package us.sustainify.commute.domain.service.google.directions;

import com.google.api.client.util.Key;

public class TransitVehicle {
	@Key
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
