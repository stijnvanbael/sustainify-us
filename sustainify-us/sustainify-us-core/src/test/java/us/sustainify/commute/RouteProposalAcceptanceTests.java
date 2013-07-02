package us.sustainify.commute;

import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class RouteProposalAcceptanceTests extends AbstractRouteAcceptanceTests {

	@Test
	public void shouldPromoteWalkingRouteWhenDistanceIsAcceptable() {
		Route walking = route(Length.kilometers(0.5), Duration.standardMinutes(5), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(0.5), Duration.standardMinutes(2), TravelMode.BICYCLING);
		Route byPublicTransit = route(Length.kilometers(0.5), Duration.standardMinutes(2), TravelMode.PUBLIC_TRANSIT);
		Route byCar = route(Length.kilometers(0.5), Duration.standardMinutes(1), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		expectRoutes(walking, byBicycle, byPublicTransit, byCar);
	}

	@Test
	public void shouldPromoteBicyclingRouteWhenDistanceIsTooFarToWalk() {
		Route walking = route(Length.kilometers(15.0), Duration.standardMinutes(120), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(15.0), Duration.standardMinutes(40), TravelMode.BICYCLING);
		Route byPublicTransit = route(Length.kilometers(15.0), Duration.standardMinutes(40), TravelMode.PUBLIC_TRANSIT);
		Route byCar = route(Length.kilometers(15.0), Duration.standardMinutes(20), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		expectRoutes(byBicycle, byPublicTransit, byCar, walking);
	}

	@Test
	public void shouldPromotePublicTransitWhenBicyclingTakesTooLong() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route byPublicTransit = route(Length.kilometers(50.0), Duration.standardMinutes(60), TravelMode.PUBLIC_TRANSIT);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		expectRoutes(byPublicTransit, byCar, byBicycle);
	}

	@Test
	public void shouldPromotePublicTransitWhenWeatherConditionsAreUnacceptable() {
		Route walking = route(Length.kilometers(15.0), Duration.standardMinutes(120), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(15.0), Duration.standardMinutes(40), TravelMode.BICYCLING);
		Route byPublicTransit = route(Length.kilometers(15.0), Duration.standardMinutes(40), TravelMode.PUBLIC_TRANSIT);
		Route byCar = route(Length.kilometers(15.0), Duration.standardMinutes(20), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(RAINY);

		expectRoutes(byPublicTransit, byCar, byBicycle, walking);
	}

	@Test
	public void shouldPromoteCarWhenPublicTransitTakesTooLong() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route byPublicTransit = route(Length.kilometers(50.0), Duration.standardMinutes(120), TravelMode.PUBLIC_TRANSIT);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		expectRoutes(byCar, byPublicTransit, byBicycle);
	}
}
