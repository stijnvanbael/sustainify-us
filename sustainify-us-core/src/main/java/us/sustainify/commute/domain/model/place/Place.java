package us.sustainify.commute.domain.model.place;

import java.util.Collections;
import java.util.Set;

import be.appify.framework.location.domain.Location;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class Place {
	private final Set<PlaceType> types;
	private final Location location;
	private final String name;

	public Place(Location location, String name, Set<PlaceType> types) {
		this.types = Sets.newHashSet(types);
		this.location = location;
		this.name = name;
	}

	public Place(Location location, String name, PlaceType... types) {
		this(location, name, Sets.newHashSet(types));
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Set<PlaceType> getTypes() {
		return Collections.unmodifiableSet(types);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Place)) {
			return false;
		}
		Place other = (Place) obj;
		return Objects.equal(this.name, other.name) && Objects.equal(this.location, other.location);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, location);
	}
}
