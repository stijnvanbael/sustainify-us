package us.sustainify.commute;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

@RunWith(MockitoJUnitRunner.class)
public class CombineTravelModesAcceptanceTests extends AbstractRouteAcceptanceTests {

	private static final Location SOUDERTON_STATION = new Location("Souderton Station", 34.502, 52.9);
	private static final Location ROCKVALE_STATION = new Location("Rockvale Station", 34.601, 52.9);

	@Test
	public void shouldProposeBicyclingToRailwayStationWhenBetween0_5kmAnd15kmOfHome() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(5), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(5.0), Duration.standardMinutes(5), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(25), TravelMode.WALKING);
		Route bicycleToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(5), TravelMode.BICYCLING);
		Route driveToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(2), TravelMode.CAR);
		routes(SOUDERTON, SOUDERTON_STATION, LocalTime.parse("08:00"), walkToStation, bicycleToStation, driveToStation);

		Route composite = route(bicycleToStation, byTrain, busToWork);

		expectRoutes(byPublicTransit, composite, byCar, byBicycle);
	}

	@Test
	public void shouldProposeDrivingToRailwayStationWhenBetween5kmAnd25kmOfHome() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(20), Duration.standardMinutes(20), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(5.0), Duration.standardMinutes(5), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(20), Duration.standardMinutes(150), TravelMode.WALKING);
		Route bicycleToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(20), Duration.standardMinutes(60), TravelMode.BICYCLING);
		Route driveToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(20), Duration.standardMinutes(10), TravelMode.CAR);
		routes(walkToStation, bicycleToStation, driveToStation);

		Route composite = route(driveToStation, byTrain, busToWork);

		expectRoutes(composite, byPublicTransit, byCar, byBicycle);
	}

	@Test
	public void shouldProposeWalkingToRailwayStationWhenBetween0kmAnd2_5kmOfHome() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.meters(450), Duration.standardMinutes(2), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(5.0), Duration.standardMinutes(5), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToStation = route(SOUDERTON, SOUDERTON_STATION, Length.meters(450), Duration.standardMinutes(5), TravelMode.WALKING);
		Route bicycleToStation = route(SOUDERTON, SOUDERTON_STATION, Length.meters(450), Duration.standardMinutes(2), TravelMode.BICYCLING);
		Route driveToStation = route(SOUDERTON, SOUDERTON_STATION, Length.meters(450), Duration.standardMinutes(1), TravelMode.CAR);
		routes(walkToStation, bicycleToStation, driveToStation);

		Route composite = route(walkToStation, byTrain, busToWork);

		expectRoutes(byPublicTransit, composite, byCar, byBicycle);
	}

	@Test
	public void shouldProposeBicyclingToWorkWhenBetween0_5kmAnd15kmOfRailwayStation() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(5), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(5.0), Duration.standardMinutes(5), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(2.5), Duration.standardMinutes(25), TravelMode.WALKING);
		Route bicycleToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(2.5), Duration.standardMinutes(5), TravelMode.BICYCLING);
		routes(walkToWork, bicycleToWork);

		Route composite = route(busToStation, byTrain, bicycleToWork);

		expectRoutes(byPublicTransit, composite, byCar, byBicycle);
	}

	@Test
	public void shouldProposeWalkingToWorkWhenBetween0kmAnd2_5kmOfRailwayStation() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(2.5), Duration.standardMinutes(5), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.meters(450), Duration.standardMinutes(2), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToWork = route(ROCKVALE_STATION, ROCKVALE, Length.meters(450), Duration.standardMinutes(5), TravelMode.WALKING);
		Route bicycleToWork = route(ROCKVALE_STATION, ROCKVALE, Length.meters(450), Duration.standardMinutes(2), TravelMode.BICYCLING);
		routes(walkToWork, bicycleToWork);

		Route composite = route(busToStation, byTrain, walkToWork);

		expectRoutes(byPublicTransit, composite, byCar, byBicycle);
	}

	@Test
	public void shouldCombineAlternativesToAndFromRailwayStations() {
		Route walking = route(Length.kilometers(50.0), Duration.standardMinutes(400), TravelMode.WALKING);
		Route byBicycle = route(Length.kilometers(50.0), Duration.standardMinutes(149), TravelMode.BICYCLING);
		Route busToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(5.5), Duration.standardMinutes(20), VehicleType.BUS);
		Route byTrain = route(SOUDERTON_STATION, ROCKVALE_STATION, Length.kilometers(40.0), Duration.standardMinutes(40), VehicleType.TRAIN);
		Route busToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(2.0), Duration.standardMinutes(5), VehicleType.BUS);
		Route byPublicTransit = route(busToStation, byTrain, busToWork);
		Route byCar = route(Length.kilometers(50.0), Duration.standardMinutes(30), TravelMode.CAR);
		routes(walking, byBicycle, byPublicTransit, byCar);
		weather(SUNNY);

		Route walkToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(5.5), Duration.standardMinutes(60), TravelMode.WALKING);
		Route bicycleToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(5.5), Duration.standardMinutes(20), TravelMode.BICYCLING);
		Route driveToStation = route(SOUDERTON, SOUDERTON_STATION, Length.kilometers(5.5), Duration.standardMinutes(10), TravelMode.CAR);
		routes(SOUDERTON, SOUDERTON_STATION, LocalTime.parse("08:00"), walkToStation, bicycleToStation, driveToStation);

		Route walkToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(2.0), Duration.standardMinutes(25), TravelMode.WALKING);
		Route bicycleToWork = route(ROCKVALE_STATION, ROCKVALE, Length.kilometers(2.0), Duration.standardMinutes(5), TravelMode.BICYCLING);
		routes(walkToWork, bicycleToWork);

		Route composite1 = route(driveToStation, byTrain, busToWork);
		Route composite2 = route(driveToStation, byTrain, bicycleToWork);
		Route composite3 = route(bicycleToStation, byTrain, busToWork);
		Route composite4 = route(busToStation, byTrain, bicycleToWork);
		Route composite5 = route(bicycleToStation, byTrain, bicycleToWork);
		Route composite6 = route(driveToStation, byTrain, walkToWork);
		Route composite7 = route(busToStation, byTrain, walkToWork);
		Route composite8 = route(bicycleToStation, byTrain, walkToWork);

		expectRoutes(composite1, composite2, byPublicTransit, composite3, composite4, composite5, byCar, composite6, composite7, composite8, byBicycle);
	}
}
