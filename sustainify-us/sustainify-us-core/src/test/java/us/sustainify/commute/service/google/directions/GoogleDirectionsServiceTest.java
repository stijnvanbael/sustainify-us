package us.sustainify.commute.service.google.directions;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.Transit;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.TravelModeSummary;
import us.sustainify.commute.domain.model.route.VehicleType;
import us.sustainify.commute.domain.repository.RouteRepository;
import us.sustainify.commute.domain.service.google.directions.GoogleDirectionsService;
import be.appify.framework.cache.Cache;
import be.appify.framework.cache.simple.HashMapCache;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;
import be.appify.framework.test.service.WebServiceClientTest;

import com.google.api.client.http.apache.ApacheHttpTransport;

public class GoogleDirectionsServiceTest extends WebServiceClientTest {
	private GoogleDirectionsService directionsService;
	private Location from;
	private Location to;
	private RouteRepository routeRepository;

	@Before
	public void before() {
		routeRepository = Mockito.mock(RouteRepository.class);
		from = new Location("Ruddervoorde", 51.09594, 3.20659);
		to = new Location("Gent", 51.0534, 3.73048);
		Cache cache = new HashMapCache();
		directionsService = new GoogleDirectionsService(new ApacheHttpTransport(), routeRepository, cache);
		directionsService.setDirectionsURL("http://localhost:" + PORT + "/maps/api/directions/json");
		addStubResponse("/maps/api/directions/json\\?.*mode=transit.*",
				"/us/sustainify/commute/service/google/directions/transit.json");
	}

	@Test
	public void testGetLocation() {
		Set<Route> routes = directionsService.findRoutes(from, to, TravelMode.PUBLIC_TRANSIT, new LocalTime());
		assertEquals(1, routes.size());

		Route route = routes.iterator().next();
		assertEquals(TravelMode.PUBLIC_TRANSIT, route.getTravelMode());
		assertEquals(Length.kilometers(55.762), route.getDistance());
		assertEquals(Duration.standardSeconds(6211), route.getDuration());

		List<Route> subroutes = route.getSubroutes();
		assertEquals(6, subroutes.size());

		Route subroute = subroutes.get(0);
		assertEquals(TravelMode.WALKING, subroute.getTravelMode());
		assertEquals(Length.meters(67), subroute.getDistance());
		assertEquals(Duration.standardSeconds(57), subroute.getDuration());

		subroute = subroutes.get(1);
		assertEquals(TravelMode.PUBLIC_TRANSIT, subroute.getTravelMode());
		assertEquals(Length.kilometers(19.397), subroute.getDistance());
		assertEquals(Duration.standardMinutes(30), subroute.getDuration());
		assertEquals(new Location("Ruddervoorde Dorp", 51.09607920, 3.20579040), subroute.getOrigin());
		assertEquals(new Location("Tielt Vierhoek", 50.99380180, 3.32602360), subroute.getDestination());
		Transit transit = subroute.getTransit();
		assertEquals(LocalTime.parse("11:40"), transit.getDepartureTime());
		assertEquals(LocalTime.parse("12:10"), transit.getArrivalTime());
		assertEquals("Brugge - Zwevezele - Tielt", transit.getHeadsign());
		assertEquals(VehicleType.BUS, transit.getVehicleType());

		subroute = subroutes.get(2);
		assertEquals(TravelMode.WALKING, subroute.getTravelMode());
		assertEquals(Length.meters(23), subroute.getDistance());
		assertEquals(Duration.standardSeconds(23), subroute.getDuration());

		subroute = subroutes.get(3);
		assertEquals(TravelMode.PUBLIC_TRANSIT, subroute.getTravelMode());
		assertEquals(Length.kilometers(33.301), subroute.getDistance());
		assertEquals(Duration.standardMinutes(57), subroute.getDuration());

		subroute = subroutes.get(4);
		assertEquals(TravelMode.PUBLIC_TRANSIT, subroute.getTravelMode());
		assertEquals(Length.kilometers(2.713), subroute.getDistance());
		assertEquals(Duration.standardMinutes(12), subroute.getDuration());

		subroute = subroutes.get(5);
		assertEquals(TravelMode.WALKING, subroute.getTravelMode());
		assertEquals(Length.meters(261), subroute.getDistance());
		assertEquals(Duration.standardSeconds(191), subroute.getDuration());

		Map<TravelMode, TravelModeSummary> summary = route.getSummary();
		assertEquals(2, summary.size());

		TravelModeSummary walkingSummary = summary.get(TravelMode.WALKING);
		assertEquals(Length.meters(351), walkingSummary.getDistance());
		assertEquals(Duration.standardSeconds(271), walkingSummary.getDuration());

		TravelModeSummary transitSummary = summary.get(TravelMode.PUBLIC_TRANSIT);
		assertEquals(Length.kilometers(55.411), transitSummary.getDistance());
		assertEquals(Duration.standardMinutes(99), transitSummary.getDuration());
	}
}
