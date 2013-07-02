package us.sustainify.commute.domain.service.google.directions;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.Transit;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;
import us.sustainify.commute.domain.repository.RouteRepository;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GoogleRouteConverter implements Function<GoogleRoute, Route> {

	private final RouteRepository routeRepository;
	private static final Map<String, VehicleType> VEHICLE_TYPES = Maps.newHashMap();
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("hh:mmaa");

	static {
		VEHICLE_TYPES.put("RAIL", VehicleType.OTHER_RAIL);
		VEHICLE_TYPES.put("METRO_RAIL", VehicleType.OTHER_RAIL);
		VEHICLE_TYPES.put("SUBWAY", VehicleType.SUBWAY);
		VEHICLE_TYPES.put("TRAM", VehicleType.TRAM);
		VEHICLE_TYPES.put("MONORAIL", VehicleType.OTHER_RAIL);
		VEHICLE_TYPES.put("HEAVY_RAIL", VehicleType.TRAIN);
		VEHICLE_TYPES.put("COMMUTER_TRAIN", VehicleType.TRAIN);
		VEHICLE_TYPES.put("HIGH_SPEED_TRAIN", VehicleType.TRAIN);
		VEHICLE_TYPES.put("BUS", VehicleType.BUS);
		VEHICLE_TYPES.put("INTERCITY_BUS", VehicleType.BUS);
		VEHICLE_TYPES.put("TROLLEYBUS", VehicleType.BUS);
		VEHICLE_TYPES.put("FERRY", VehicleType.SHIP);
		VEHICLE_TYPES.put("CABLE_CAR", VehicleType.TRAM);
		VEHICLE_TYPES.put("GONDOLA_LIFT", VehicleType.GONDOLA);
		VEHICLE_TYPES.put("FUNICULAR", VehicleType.OTHER_RAIL);
	}

	public GoogleRouteConverter(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Override
	@Nullable
	public Route apply(@Nullable GoogleRoute googleRoute) {
		String code = googleRoute.getOverview_polyline().getPoints();
		Route route = routeRepository.findByCode(code);
		if (route == null) {
			long totalDuration = 0;
			long totalDistance = 0;
			Location from = null;
			Location to = null;
			List<Route> subroutes = Lists.newArrayList();
			for (RouteLeg leg : googleRoute.getLegs()) {
				totalDuration += leg.getDuration().getValue();
				totalDistance += leg.getDistance().getValue();
				if (from == null) {
					from = new Location(leg.getStart_address(),
							leg.getStart_location().getLat(),
							leg.getStart_location().getLng());
				}
				to = new Location(leg.getEnd_address(),
						leg.getEnd_location().getLat(),
						leg.getEnd_location().getLng());
				for (RouteStep step : leg.getSteps()) {
					Route subroute = toSubroute(step);
					subroutes.add(subroute);
				}
			}
			route = Route.from(from).to(to).duration(Duration.standardSeconds(totalDuration))
					.distance(Length.meters(totalDistance)).subroutes(subroutes).code(code).build();
		}
		return route;
	}

	private Route toSubroute(RouteStep step) {

		Coordinates startLocation = step.getStart_location();
		Coordinates endLocation = step.getEnd_location();
		Location from = new Location(startLocation.getLat(), startLocation.getLng());
		Location to = new Location(endLocation.getLat(), endLocation.getLng());
		Transit transit = null;
		if (step.getTransit_details() != null) {
			TransitDetails transitDetails = step.getTransit_details();
			from = new Location(transitDetails.getDeparture_stop().getName(), startLocation.getLat(), startLocation.getLng());
			to = new Location(transitDetails.getArrival_stop().getName(), endLocation.getLat(), endLocation.getLng());
			transit = createTransit(step.getTransit_details());
		}
		int duration = step.getDuration().getValue();
		int distance = step.getDistance().getValue();
		TravelMode travelMode = travelModeOf(step.getTravel_mode());
		String code = step.getPolyline().getPoints();
		Route route = Route.from(from).to(to).duration(Duration.standardSeconds(duration))
				.distance(Length.meters(distance)).travelMode(travelMode).code(code).build();
		route.setTransit(transit);
		return route;
	}

	private Transit createTransit(TransitDetails transitDetails) {
		Transit transit = new Transit();
		transit.setArrivalTime(LocalTime.parse(transitDetails.getArrival_time().getText(), TIME_FORMAT));
		transit.setDepartureTime(LocalTime.parse(transitDetails.getDeparture_time().getText(), TIME_FORMAT));
		transit.setHeadsign(transitDetails.getHeadsign());
		transit.setVehicleType(vehicleTypeOf(transitDetails.getLine().getVehicle().getType()));
		return transit;
	}

	private VehicleType vehicleTypeOf(String vehicleType) {
		if (VEHICLE_TYPES.containsKey(vehicleType)) {
			return VEHICLE_TYPES.get(vehicleType);
		}
		return VehicleType.OTHER;
	}

	private TravelMode travelModeOf(String travelMode) {
		if ("TRANSIT".equals(travelMode)) {
			return TravelMode.PUBLIC_TRANSIT;
		} else if ("WALKING".equals(travelMode)) {
			return TravelMode.WALKING;
		} else if ("BICYCLING".equals(travelMode)) {
			return TravelMode.BICYCLING;
		} else if ("DRIVING".equals(travelMode)) {
			return TravelMode.CAR;
		}
		return null;
	}

}
