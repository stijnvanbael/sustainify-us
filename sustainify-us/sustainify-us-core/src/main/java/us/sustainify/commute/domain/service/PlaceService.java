package us.sustainify.commute.domain.service;

import java.util.Set;

import us.sustainify.commute.domain.model.place.Place;
import us.sustainify.commute.domain.model.place.PlaceType;
import be.appify.framework.location.domain.Location;

public interface PlaceService {
	Set<Place> findPlacesNearby(Location location, PlaceType type, int radiusInMeters);
}
