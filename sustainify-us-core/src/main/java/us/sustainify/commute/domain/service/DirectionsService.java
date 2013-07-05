package us.sustainify.commute.domain.service;

import java.util.Set;

import org.joda.time.LocalTime;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.location.domain.Location;

public interface DirectionsService {
	Set<Route> findRoutes(Location from, Location to, TravelMode travelMode, LocalTime arrivalTime);
}
