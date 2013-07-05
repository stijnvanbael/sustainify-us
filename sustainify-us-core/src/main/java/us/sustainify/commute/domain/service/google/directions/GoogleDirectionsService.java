package us.sustainify.commute.domain.service.google.directions;

import java.util.Map;
import java.util.Set;

import javassist.tools.rmi.RemoteException;

import javax.annotation.Nullable;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.repository.RouteRepository;
import us.sustainify.commute.domain.service.DirectionsService;
import be.appify.framework.cache.Cache;
import be.appify.framework.common.service.AbstractCachingServiceClient;
import be.appify.framework.location.domain.Location;
import be.appify.framework.util.TypeBuilder;

import com.google.api.client.http.HttpTransport;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GoogleDirectionsService extends AbstractCachingServiceClient implements DirectionsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDirectionsService.class);
	private String directionsURL = "https://maps.googleapis.com/maps/api/directions/json";
	private final Function<GoogleRoute, Route> routeConverter;
	private int delay = 250;

	public GoogleDirectionsService(HttpTransport transport, RouteRepository routeRepository, Cache cache) {
		super(transport, cache);
		routeConverter = new GoogleRouteConverter(routeRepository);
	}

	public void setDirectionsURL(String directionsURL) {
		this.directionsURL = directionsURL;
	}

	@Override
	public Set<Route> findRoutes(final Location from, final Location to, final TravelMode travelMode, final LocalTime arrivalTime) {
		final Map<String, String> parameters = Maps.newHashMap();
		parameters.put("origin", from.getLatitude() + "," + from.getLongitude());
		parameters.put("destination", to.getLatitude() + "," + to.getLongitude());
		parameters.put("mode", toString(travelMode));
		if (travelMode == TravelMode.PUBLIC_TRANSIT) {
			parameters.put("arrival_time", Long.toString(arrivalTime.getMillisOfDay() / 1000));
		}
		parameters.put("sensor", "false");
		return callServiceCacheResult(DirectionsMessage.class, new TypeBuilder<Set<Route>>() {
		}.build(), directionsURL, parameters,
				new Function<DirectionsMessage, Set<Route>>() {

					@Override
					@Nullable
					public Set<Route> apply(@Nullable DirectionsMessage message) {
						Set<Route> routes = Sets.newHashSet();
						if ("OVER_QUERY_LIMIT".equals(message.getStatus())) {
							LOGGER.warn("Google Directions service is over query limit");
							try {
								Thread.sleep(delay);
								delay += 250;
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
							routes = findRoutes(from, to, travelMode, arrivalTime);
						} else if ("OK".equals(message.getStatus()) || "ZERO_RESULTS".equals(message.getStatus())) {
							delay = 250;
							if (message.getRoutes() != null && !message.getRoutes().isEmpty()) {
								for (GoogleRoute googleRoute : message.getRoutes()) {
									routes.add(routeConverter.apply(googleRoute));
								}
							}
						} else {
							throw new RemoteException("Error " + message.getStatus() + " for URL " + constructURL(directionsURL, parameters));
						}
						return routes;
					}
				});
	}

	private String toString(TravelMode travelMode) {
		switch (travelMode) {
		case BICYCLING:
			return "bicycling";
		case CAR:
			return "driving";
		case PUBLIC_TRANSIT:
			return "transit";
		case WALKING:
			return "walking";
		}
		return "driving";
	}
}
