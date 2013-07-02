package us.sustainify.commute.domain.service.google.places;

import com.google.api.client.util.Key;

public class PlaceLocation {
	@Key
	private double lat;

	@Key
	private double lng;

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
}
