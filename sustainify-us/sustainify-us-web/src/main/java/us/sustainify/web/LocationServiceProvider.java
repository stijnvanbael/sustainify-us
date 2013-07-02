package us.sustainify.web;

import java.util.List;

import be.appify.framework.location.service.LocationService;

import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.*;
import com.google.sitebricks.http.Get;

@At("/locations")
@Service
public class LocationServiceProvider {
	private final LocationService locationService;
	private String name;

	@Inject
	public LocationServiceProvider(LocationService locationService) {
		this.locationService = locationService;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Get
	public Reply<List<String>> getLocations() {
		return Reply.with(locationService.getLocations(name)).as(Json.class);
	}
}
