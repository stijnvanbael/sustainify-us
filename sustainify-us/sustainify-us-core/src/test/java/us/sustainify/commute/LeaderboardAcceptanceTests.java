package us.sustainify.commute;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.common.domain.model.organisation.OfficeDay;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class LeaderboardAcceptanceTests extends AbstractRouteAcceptanceTests {

	private SustainifyUser sarah;
	private SustainifyUser john;

	@Override
	@Before
	public void before() {
		super.before();
		sarah = SustainifyUser.createNew().firstName("Sarah").lastName("Graham").organisation(APPIFY).emailAddress("sarah.graham@appify.be")
				.build();
		john = SustainifyUser.createNew().firstName("John").lastName("Doe").organisation(APPIFY).emailAddress("john.doe@appify.be")
				.build();
		sarah.getPreferences().setHomeLocation(SOUDERTON);
		john.getPreferences().setHomeLocation(SOUDERTON);
		OfficeDay officeDay = sarah.getPreferences().getOfficeHours().get(TODAY.getStart().getDayOfWeek());
		officeDay.setArrival(LocalTime.parse("08:00"));
		officeDay.setDeparture(LocalTime.parse("17:00"));
		officeDay = john.getPreferences().getOfficeHours().get(TODAY.getStart().getDayOfWeek());
		officeDay.setArrival(LocalTime.parse("08:00"));
		officeDay.setDeparture(LocalTime.parse("17:00"));
	}

	@Test
	public void confirmRouteShouldAwardPointsAndMakeChoiceVisibleToCoworkers() {
		Route byCar = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(5)).travelMode(TravelMode.CAR)
				.build();
		Route byBicyle = Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
				.travelMode(TravelMode.BICYCLING).build();
		routes(
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(50))
						.travelMode(TravelMode.WALKING).build(),
				byBicyle,
				Route.from(SOUDERTON).to(ROCKVALE).distance(Length.kilometers(5.0)).duration(Duration.standardMinutes(15))
						.travelMode(TravelMode.PUBLIC_TRANSIT).build(),
				byCar);
		weather(SUNNY);

		chooseRoute(john, byCar);
		chooseRoute(sarah, byBicyle);

		assertLeaderboard(1, sarah, 50);
		assertLeaderboard(2, john, 5);
	}
}
