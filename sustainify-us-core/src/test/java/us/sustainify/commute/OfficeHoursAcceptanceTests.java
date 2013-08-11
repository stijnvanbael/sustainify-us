package us.sustainify.commute;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class OfficeHoursAcceptanceTests extends AbstractRouteAcceptanceTests {

    @Test
    public void transitRouteShouldBeCalculatedForTodaysArrivalTime() {
        user.getPreferences().getOfficeHours().get(TODAY.getStart().getDayOfWeek() - 1).setArrival(LocalTime.parse("08:30"));

        Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
        Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(150), TravelMode.BICYCLING);
        Route byPublicTransit = route(SOUDERTON, ROCKVALE, Length.kilometers(50.0), Duration.standardMinutes(60), VehicleType.BUS, LocalTime.parse("08:30"));
        Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
        routes(SOUDERTON, ROCKVALE, LocalTime.parse("08:30"), walking, byBicycle, byPublicTransit, byCar);
        weather(SUNNY);

        calculateRoutes();
        Mockito.verify(directionsService).findRoutes(SOUDERTON, ROCKVALE, TravelMode.PUBLIC_TRANSIT, LocalTime.parse("08:30"));
    }

    @Test
    public void transitRouteShouldNotBeCalculatedOnNonWorkingDays() {
        user.getPreferences().getOfficeHours().get(TODAY.getStart().getDayOfWeek() - 1).setArrival(null);

        Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
        Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(150), TravelMode.BICYCLING);
        Route byPublicTransit = route(SOUDERTON, ROCKVALE, Length.kilometers(50.0), Duration.standardMinutes(60), VehicleType.BUS, LocalTime.parse("08:30"));
        Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
        routes(SOUDERTON, ROCKVALE, LocalTime.parse("08:30"), walking, byBicycle, byPublicTransit, byCar);
        weather(SUNNY);

        calculateRoutes();
        Mockito.verify(directionsService, Mockito.never()).findRoutes(Mockito.eq(SOUDERTON), Mockito.eq(ROCKVALE), Mockito.any(TravelMode.class), Mockito.any(LocalTime.class));
    }
}