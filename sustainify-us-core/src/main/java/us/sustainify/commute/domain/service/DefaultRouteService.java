package us.sustainify.commute.domain.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.desirability.DesirabilityScore;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DefaultRouteService implements RouteService {

	protected static final ReadableDuration MAX_DURATION = Duration.standardMinutes(150);
	private final DirectionsService directionsService;
	private final Set<DesirabilityScore> desirabilityScores;
	private final Logger LOGGER = LoggerFactory.getLogger(DefaultRouteService.class);

	public DefaultRouteService(DirectionsService directionsService, DesirabilityScore... desirabilityScores) {
		this.directionsService = directionsService;
		this.desirabilityScores = Sets.newHashSet(desirabilityScores);
	}

	@Override
	public List<Route> findRoutesFor(SustainifyUser user, OrganisationLocation destination) {
		if (user.getPreferences() == null || user.getPreferences().getHomeLocation() == null) {
			return Collections.emptyList();
		}
		Location from = user.getPreferences().getHomeLocation();
		Location to = destination.getLocation();
        int dayOfWeek = new DateTime().getDayOfWeek() - 1;
        LocalTime arrivalTime = null;
        if(dayOfWeek < user.getPreferences().getOfficeHours().size()) {
            arrivalTime = user.getPreferences().getOfficeHours().get(dayOfWeek).getArrival();
        }
		List<Route> routes = Lists.newArrayList();
		if (arrivalTime != null) {
            addSimpleRoutes(from, to, routes, arrivalTime);
            addCombinedRoutes(from, to, routes, arrivalTime);
            routes = Lists.newArrayList(Collections2.filter(routes, new Predicate<Route>() {
                @Override
                public boolean apply(@Nullable Route route) {
                    return route.getDuration().isShorterThan(MAX_DURATION);
                }
            }));
            sortByDesirability(routes);
        }
		return routes;
	}

	private void addSimpleRoutes(Location from, Location to, List<Route> routes, LocalTime arrivalTime) {
		for (TravelMode travelMode : TravelMode.values()) {
			for (Route route : directionsService.findRoutes(from, to, travelMode, arrivalTime)) {
				routes.add(Route.compose(route).build());
			}
		}
	}

	private void addCombinedRoutes(Location from, Location to, List<Route> routes, LocalTime arrivalTime) {
		Route railRoute = findRailRoute(routes);
		if (railRoute != null) {
			Route homeToStation = routeToStation(railRoute);
			Route stationToWork = routeFromStation(railRoute);
			Route trainRide = trainRide(railRoute);

			Location departureStation = homeToStation != null ? homeToStation.getDestination() : from;
			Location arrivalStation = stationToWork != null ? stationToWork.getOrigin() : to;

			Set<Route> routesToStation = Sets.newHashSet();
			routesToStation.addAll(routesBetween(Length.meters(500), Length.kilometers(15),
					directionsService.findRoutes(from, departureStation, TravelMode.BICYCLING, arrivalTime)));
			routesToStation.addAll(routesBetween(Length.kilometers(5), Length.kilometers(25),
					directionsService.findRoutes(from, departureStation, TravelMode.CAR, arrivalTime)));
			routesToStation.addAll(routesBetween(Length.meters(0), Length.meters(2500),
					directionsService.findRoutes(from, departureStation, TravelMode.WALKING, arrivalTime)));

			Set<Route> routesToWork = Sets.newHashSet();
			routesToWork.addAll(routesBetween(Length.meters(500), Length.kilometers(15),
					directionsService.findRoutes(arrivalStation, to, TravelMode.BICYCLING, arrivalTime)));
			routesToWork.addAll(routesBetween(Length.meters(0), Length.meters(2500),
					directionsService.findRoutes(arrivalStation, to, TravelMode.WALKING, arrivalTime)));

			if (departureStation != from) {
				for (Route route : routesToStation) {
					routes.add(buildRoute(route, trainRide, stationToWork));
				}
			}
			if (arrivalStation != to) {
				for (Route route : routesToWork) {
					routes.add(buildRoute(homeToStation, trainRide, route));
				}
			}
			if (departureStation != from && arrivalStation != to) {
				for (Route route1 : routesToStation) {
					for (Route route2 : routesToWork) {
						routes.add(buildRoute(route1, trainRide, route2));
					}
				}
			}
		}
	}

	private Collection<? extends Route> routesBetween(final Length min, final Length max, Set<Route> routes) {
		return Collections2.filter(routes, new Predicate<Route>() {
			@Override
			public boolean apply(@Nullable Route route) {
				return route.getDistance().longerThan(min) && route.getDistance().shorterThan(max);
			}
		});
	}

	private Route buildRoute(Route homeToStation, Route trainRide, Route stationToWork) {
		List<Route> routes = Lists.newArrayList();
		if (homeToStation != null) {
			routes.add(homeToStation);
		}
		routes.add(trainRide);
		if (stationToWork != null) {
			routes.add(stationToWork);
		}
		return Route.compose(routes).build();
	}

	private Route trainRide(Route route) {
		boolean departed = false;
		List<Route> routes = Lists.newArrayList();
		for (Route subroute : route.getSubroutes()) {
			if (subroute.getTransit() != null) {
				VehicleType vehicle = subroute.getTransit().getVehicleType();
				if (vehicle == VehicleType.TRAIN) {
					departed = true;
					routes.add(subroute);
				} else if (departed) {
					return routes.isEmpty() ? null : Route.compose(routes).build();
				}
			}
		}
		return routes.isEmpty() ? null : Route.compose(routes).build();
	}

	private Route routeFromStation(Route route) {
		boolean departed = false;
		boolean arrived = false;
		List<Route> routes = Lists.newArrayList();
		for (Route subroute : route.getSubroutes()) {
			if (subroute.getTransit() != null) {
				VehicleType vehicle = subroute.getTransit().getVehicleType();
				if (vehicle == VehicleType.TRAIN && !arrived) {
					departed = true;
				} else if (departed) {
					arrived = true;
					routes.add(subroute);
				}
			}
		}
		return routes.isEmpty() ? null : Route.compose(routes).build();
	}

	private Route routeToStation(Route route) {
		List<Route> routes = Lists.newArrayList();
		for (Route subroute : route.getSubroutes()) {
			if (subroute.getTransit() != null) {
				VehicleType vehicle = subroute.getTransit().getVehicleType();
				if (vehicle == VehicleType.TRAIN) {
					return routes.isEmpty() ? null : Route.compose(routes).build();
				} else {
					routes.add(subroute);
				}
			}
		}
		throw new IllegalArgumentException(route + " is not a train route");
	}

	private Route findRailRoute(List<Route> routes) {
		for (Route route : routes) {
			route = route.getSubroutes().get(0);
			if (route.getTravelMode() == TravelMode.PUBLIC_TRANSIT) {
				for (Route subroute : route.getSubroutes()) {
					if (subroute.getTransit() != null) {
						VehicleType vehicle = subroute.getTransit().getVehicleType();
						if (vehicle == VehicleType.TRAIN) {
							return route;
						}
					}
				}
			}
		}
		return null;
	}

	private void sortByDesirability(List<Route> routes) {
		final Map<Route, Integer> desirabilityScoreCache = Maps.newHashMap();
		Collections.sort(routes, new Comparator<Route>() {
			@Override
			public int compare(Route route1, Route route2) {
				return -desirability(route1, desirabilityScoreCache).compareTo(desirability(route2, desirabilityScoreCache));
			}
		});
	}

	private Integer desirability(Route route, Map<Route, Integer> desirabilityScoreCache) {
		Integer desirability = desirabilityScoreCache.get(route);
		if (desirability == null) {
			desirability = 0;
			for (DesirabilityScore desirabilityScore : desirabilityScores) {
				desirability += desirabilityScore.forRoute(route);
			}
			desirabilityScoreCache.put(route, desirability);
			LOGGER.debug("Desirability for route {}: {}", route, desirability);
		}
		return desirability;
	}

}
