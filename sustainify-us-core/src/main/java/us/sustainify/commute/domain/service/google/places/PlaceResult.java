package us.sustainify.commute.domain.service.google.places;

import java.util.Set;

import com.google.api.client.util.Key;

public class PlaceResult {
	@Key
	private PlaceGeometry geometry;

	@Key
	private String id;

	@Key
	private String name;

	@Key
	private Set<String> types;

	public PlaceGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(PlaceGeometry geometry) {
		this.geometry = geometry;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public Set<String> getTypes() {
		return types;
	}
}
