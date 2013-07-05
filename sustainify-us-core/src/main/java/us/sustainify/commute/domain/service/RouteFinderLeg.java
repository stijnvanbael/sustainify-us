package us.sustainify.commute.domain.service;

import java.util.Set;

import javax.annotation.Nullable;

import org.joda.time.LocalTime;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class RouteFinderLeg {

	private TravelMode travelMode;
	private final Set<Location> origins;
	private Length minDistance;
	private Length maxDistance;
	private Set<Location> destinations;
	private final Set<Route> routes;

	public RouteFinderLeg(Location origin) {
		this(Sets.newHashSet(origin));
	}

	public RouteFinderLeg(Set<Location> origins) {
		this.origins = origins;
		this.routes = Sets.newHashSet();
	}

	public void travelMode(TravelMode travelMode) {
		this.travelMode = travelMode;
	}

	public void minDistance(Length minDistance) {
		this.minDistance = minDistance;
	}

	public void maxDistance(Length maxDistance) {
		this.maxDistance = maxDistance;
	}

	public void destinations(Set<Location> destinations) {
		if (travelMode == null) {
			throw new IllegalStateException("Call using(TravelMode) first.");
		}
		this.destinations = destinations;
	}

	public void destination(Location destination) {
		destinations(Sets.newHashSet(destination));
	}

	public void findRoutes(DirectionsService directionsService, LocalTime arrivalTime) {
		for (Location origin : origins) {
			for (Location destination : destinations) {
				Length distance = origin.distanceTo(destination);
				if (maxDistance != null ? distance.shorterThan(maxDistance) : true) {
					Set<Route> routesFound = directionsService.findRoutes(origin, destination, travelMode, arrivalTime);
					Set<Route> filteredRoutes = Sets.filter(routesFound, new Predicate<Route>() {
						@Override
						public boolean apply(@Nullable Route input) {
							return (minDistance != null ? input.getDistance().longerThan(minDistance) : true)
									&& (maxDistance != null ? input.getDistance().shorterThan(maxDistance) : true);
						}
					});
					routes.addAll(filteredRoutes);
				}
			}
		}
	}

	public Set<Route> getRoutes() {
		return routes;
	}

}
