package us.sustainify.commute;

import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmRouteAcceptanceTests extends AbstractRouteAcceptanceTests {

	@Test
	public void confirmRouteShouldAwardPointsAndMakeChoiceVisibleToCoworkers() {
		Route byBicycle = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.BICYCLING).build();
		routes(
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(50))
						.travelMode(TravelMode.WALKING).build(),
				byBicycle,
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
						.travelMode(TravelMode.PUBLIC_TRANSIT).build(),
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(5)).travelMode(TravelMode.CAR)
						.build());
		weather(SUNNY);

		chooseRoute(user, byBicycle);
		assertRouteChosen(user, byBicycle);
		assertPointsAwarded(user, 50);
	}

	@Test
	public void reconfirmRouteShouldUpdateAwardedPointsAndShowNewChoiceToCoworkers() {
		Route byCar = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(5)).travelMode(TravelMode.CAR)
				.build();
		Route byBicycle = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.BICYCLING).build();
		routes(
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(50))
						.travelMode(TravelMode.WALKING).build(),
				byBicycle,
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
						.travelMode(TravelMode.PUBLIC_TRANSIT).build(),
				byCar);
		weather(SUNNY);

		chooseRoute(user, byBicycle);
		chooseRoute(user, byCar);
		assertRouteChosen(user, byCar);
		assertPointsAwarded(user, 5);
	}
}
