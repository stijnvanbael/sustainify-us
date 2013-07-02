package us.sustainify.commute;

import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class ScoreAcceptanceTests extends AbstractRouteAcceptanceTests {

	@Test
	public void shouldNotModififyScoresForAcceptableWeather() {
		Route walking = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(50))
				.travelMode(TravelMode.WALKING).build();
		Route byBicycle = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.BICYCLING).build();
		Route byPublicTransit = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.PUBLIC_TRANSIT).build();
		Route byCar = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(5)).travelMode(TravelMode.CAR)
				.build();
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		assertScore(user, byBicycle, 50);
		assertScore(user, walking, 50);
		assertScore(user, byPublicTransit, 25);
		assertScore(user, byCar, 5);
	}

	@Test
	public void shouldModififyScoresForUnacceptableWeather() {
		Route walking = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(50))
				.travelMode(TravelMode.WALKING).build();
		Route byBicycle = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.BICYCLING).build();
		Route byPublicTransit = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.PUBLIC_TRANSIT).build();
		Route byCar = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(5)).travelMode(TravelMode.CAR)
				.build();
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(RAINY);

		assertScore(user, byBicycle, 100);
		assertScore(user, walking, 100);
		assertScore(user, byPublicTransit, 25);
		assertScore(user, byCar, 5);
	}
}
