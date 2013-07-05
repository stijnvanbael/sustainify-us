package us.sustainify.commute.domain.service.google.directions;

import com.google.api.client.util.Key;

public class TransitStop {
	@Key
	private Coordinates location;

	@Key
	private String name;

	public Coordinates getLocation() {
		return location;
	}

	public void setLocation(Coordinates location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
