package us.sustainify.commute.domain.service.google.directions;

import java.util.Set;

import com.google.api.client.util.Key;

public class DirectionsMessage {
	@Key
	private Set<GoogleRoute> routes;

	@Key
	private String status;

	public Set<GoogleRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(Set<GoogleRoute> routes) {
		this.routes = routes;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
