package us.sustainify.commute.domain.service;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalTime;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class RouteFinder {

	private final DirectionsService directionsService;
	private final List<RouteFinderLeg> routeLegs;
	private RouteFinderLeg currentLeg;
	private final LocalTime arrivalTime;

	public RouteFinder(Location origin, DirectionsService directionsService, LocalTime arrivalTime) {
		this.directionsService = directionsService;
		routeLegs = Lists.newArrayList();
		currentLeg = new RouteFinderLeg(origin);
		routeLegs.add(currentLeg);
		this.arrivalTime = arrivalTime;
	}

	public RouteFinder using(TravelMode travelMode) {
		currentLeg.travelMode(travelMode);
		return this;
	}

	public RouteFinder minDistance(Length minDistance) {
		currentLeg.minDistance(minDistance);
		return this;
	}

	public RouteFinder maxDistance(Length maxDistance) {
		currentLeg.maxDistance(maxDistance);
		return this;
	}

	public RouteFinder viaOneOf(Set<Location> locations) {
		currentLeg.destinations(locations);
		currentLeg.findRoutes(directionsService, arrivalTime);
		RouteFinderLeg nextLeg = new RouteFinderLeg(locations);
		routeLegs.add(nextLeg);
		currentLeg = nextLeg;
		return this;
	}

	public Route to(Location to) {
		currentLeg.destination(to);
		currentLeg.findRoutes(directionsService, arrivalTime);
		return buildRoute();
	}

	private Route buildRoute() {
		Set<Route> routes = Sets.newHashSet();
		for (RouteFinderLeg leg : routeLegs) {
			Set<Route> legRoutes = leg.getRoutes();
			if (legRoutes.isEmpty()) {
				return null;
			}
			if (routes.isEmpty()) {
				routes.addAll(legRoutes);
			} else {
				routes = combine(routes, legRoutes);
			}
		}
		return fastest(routes);
	}

	private Set<Route> combine(Set<Route> routes, Set<Route> nextRoutes) {
		Set<Route> combinedRoutes = Sets.newHashSet();
		for (Route route : routes) {
			for (Route nextRoute : nextRoutes) {
				if (route.getDestination().equals(nextRoute.getOrigin())) {
					List<Route> subroutes = Lists.newArrayList();
					if (route.getSubroutes().isEmpty()) {
						subroutes.add(route);
					} else {
						subroutes.addAll(route.getSubroutes());
					}
					subroutes.add(nextRoute);
					Route combinedRoute = Route.compose(subroutes).build();
					combinedRoutes.add(combinedRoute);
				}
			}
		}
		return combinedRoutes;
	}

	private Route fastest(Set<Route> routes) {
		Route fastest = null;
		for (Route route : routes) {
			if (fastest == null || route.getDuration().isShorterThan(fastest.getDuration())) {
				fastest = route;
			}
		}
		return fastest;
	}
}
