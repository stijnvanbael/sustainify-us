package us.sustainify.commute.domain.service.google.places;

import com.google.api.client.util.Key;

public class PlaceGeometry {
	@Key
	private PlaceLocation location;

	public PlaceLocation getLocation() {
		return location;
	}

	public void setLocation(PlaceLocation location) {
		this.location = location;
	}
}
