package us.sustainify.commute;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import us.sustainify.common.domain.model.organisation.*;
import us.sustainify.commute.domain.model.desirability.DesirabilityScore;
import us.sustainify.commute.domain.model.desirability.WeatherScore;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.route.Transit;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;
import us.sustainify.commute.domain.model.score.RouteScore;
import us.sustainify.commute.domain.model.score.UserScore;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.commute.domain.service.DefaultRouteChoiceService;
import us.sustainify.commute.domain.service.DefaultRouteService;
import us.sustainify.commute.domain.service.DefaultScoreService;
import us.sustainify.commute.domain.service.DirectionsService;
import us.sustainify.commute.domain.service.RouteChoiceService;
import us.sustainify.commute.domain.service.RouteService;
import us.sustainify.commute.domain.service.ScoreService;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Speed;
import be.appify.framework.quantity.Temperature;
import be.appify.framework.security.domain.User;
import be.appify.framework.weather.domain.WeatherCondition;
import be.appify.framework.weather.domain.WeatherConditionType;
import be.appify.framework.weather.service.WeatherService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractRouteAcceptanceTests {
	protected static final Location SOUDERTON = new Location("Souderton, Sustainifyland", 34.5, 52.9);
	protected static final Location ROCKVALE = new Location("Rockvale, Sustainifyland", 34.6, 52.9);
	private static final OrganisationLocation ORGANISATION_LOCATION = new OrganisationLocation("Rockvale", ROCKVALE);
	protected static final Organisation APPIFY = new Organisation("Appify", ORGANISATION_LOCATION);
	protected static final Interval TODAY = today();
	protected static final WeatherCondition RAINY = WeatherCondition.on(TODAY)
			.minTemperature(Temperature.degreesCelcius(18))
			.maxTemperature(Temperature.degreesCelcius(22))
			.cloudCoverage(0.9)
			.precipitation(Length.millimeters(5))
			.windSpeed(Speed.kilometersPerHour(0))
			.conditionType(WeatherConditionType.OVERCAST)
			.build();
	protected static final WeatherCondition SUNNY = WeatherCondition.on(TODAY)
			.minTemperature(Temperature.degreesCelcius(18))
			.maxTemperature(Temperature.degreesCelcius(22))
			.cloudCoverage(0.0)
			.precipitation(Length.millimeters(0))
			.windSpeed(Speed.kilometersPerHour(0))
			.conditionType(WeatherConditionType.CLEAR)
			.build();

	protected static Interval today() {
		DateTime start = new DateMidnight().toDateTime();
		DateTime end = start.plusDays(1).minusMillis(1);
		return new Interval(start, end);
	}

	protected RouteService routeService;
	private ScoreService scoreService;
	protected SustainifyUser user;
	private RouteChoiceService routeChoiceService;

	@Mock
	private WeatherService weatherService;

	@Mock
	protected DirectionsService directionsService;
	protected WeatherScore weatherScore;

	@Mock
	private ScoredRouteRepository scoredRouteRepository;

	private final List<ScoredRoute> storedRoutes = Lists.newArrayList();

	@Before
	public void before() {
		weatherScore = WeatherScore.withService(weatherService).base(100)
				.subtract(1).perDegreeBelow(Temperature.degreesCelcius(15))
				.subtract(1).perDegreeAbove(Temperature.degreesCelcius(25))
				.subtract(10).perMillimeterPrecipitation()
				.subtract(1).perKilometerPerHourWindSpeedAbove(Speed.kilometersPerHour(10));
		routeService = new DefaultRouteService(directionsService,
				DesirabilityScore.forTravelMode(TravelMode.BICYCLING).base(0).subtract(1).perMinute().addWeatherScore(weatherScore),
				DesirabilityScore.forTravelMode(TravelMode.WALKING).base(20).subtract(1).perMinute().addWeatherScore(weatherScore),
				DesirabilityScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).base(80).subtract(1).perMinute(),
				DesirabilityScore.forTravelMode(TravelMode.CAR).base(40).subtract(1).perMinute());
		user = SustainifyUser.createNew().firstName("Sarah").lastName("Graham").organisation(APPIFY).emailAddress("sarah.graham@appify.be")
				.build();
		user.getPreferences().setHomeLocation(SOUDERTON);
        for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
            OfficeDay officeDay = new OfficeDay();
            officeDay.setDayOfWeek(dayOfWeek);
            officeDay.setUser(user);
            user.getPreferences().getOfficeHours().add(officeDay);
        }
		OfficeDay officeDay = user.getPreferences().getOfficeHours().get(TODAY.getStart().getDayOfWeek() - 1);
		officeDay.setArrival(LocalTime.parse("08:00"));
		officeDay.setDeparture(LocalTime.parse("17:00"));

		scoreService = new DefaultScoreService(
				RouteScore.forTravelMode(TravelMode.BICYCLING).perKilometer(10).addWeatherScore(weatherScore),
				RouteScore.forTravelMode(TravelMode.WALKING).perKilometer(10).addWeatherScore(weatherScore),
				RouteScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).perKilometer(5),
				RouteScore.forTravelMode(TravelMode.CAR).perKilometer(1));
		routeChoiceService = new DefaultRouteChoiceService(scoredRouteRepository);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				storedRoutes.add((ScoredRoute) invocation.getArguments()[0]);
				return null;
			}
		}).when(scoredRouteRepository).store(Mockito.any(ScoredRoute.class));
		Mockito.when(scoredRouteRepository.findRecentRoutesByOrganisation(APPIFY, 20)).thenReturn(storedRoutes);
		Mockito.when(scoredRouteRepository.findRoutesByOrganisationOfLastDays(APPIFY, 7)).thenReturn(storedRoutes);
		Mockito.when(scoredRouteRepository.findRouteByUserAndDay(Mockito.any(User.class), Mockito.any(LocalDate.class)))
				.thenAnswer(new Answer<ScoredRoute>() {

					@Override
					public ScoredRoute answer(InvocationOnMock invocation) throws Throwable {
						for (ScoredRoute route : storedRoutes) {
							if (route.getUser().equals(invocation.getArguments()[0]) && route.getDay().equals(invocation.getArguments()[1])) {
								return route;
							}
						}
						return null;
					}
				});
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				storedRoutes.remove(invocation.getArguments()[0]);
				return null;
			}
		}).when(scoredRouteRepository).delete(Mockito.any(ScoredRoute.class));
	}

	protected void expectRoutes(Route... expectedRoutes) {
		List<Route> routes = calculateRoutes();

		assertEquals("Number of routes: ", expectedRoutes.length, routes.size());

		Iterator<Route> iterator = routes.iterator();
		int i = 0;
		for (Route expectedRoute : expectedRoutes) {
			Route actualRoute = iterator.next();
			if (expectedRoute.getSubroutes().isEmpty()) {
				assertEquals("Route " + (i + 1) + ": ", expectedRoute.getTravelMode(), actualRoute.getTravelMode());
				assertEquals("Route " + (i + 1) + ": ", expectedRoute.getDistance(), actualRoute.getDistance());
			} else {
				if (expectedRoute.getSubroutes().size() > 1 && actualRoute.getSubroutes().size() == 1) {
					actualRoute = actualRoute.getSubroutes().get(0);
				}
				assertEquals("Subroutes expected for route " + (i + 1) + ": ", expectedRoute.getSubroutes().size(), actualRoute.getSubroutes().size());
				Iterator<Route> subrouteIterator = actualRoute.getSubroutes().iterator();
				int j = 0;
				for (Route expectedSubroute : expectedRoute.getSubroutes()) {
					Route actualSubroute = subrouteIterator.next();
					assertEquals("Route " + (i + 1) + ", subroute " + (j + 1) + ": ", expectedSubroute.getTravelMode(), actualSubroute.getTravelMode());
					assertEquals("Route " + (i + 1) + ", subroute " + (j + 1) + ": ", expectedSubroute.getDistance(), actualSubroute.getDistance());
					j++;
				}
			}
			i++;
		}
	}

	protected List<Route> calculateRoutes() {
		return routeService.findRoutesFor(user, ORGANISATION_LOCATION);
	}

	protected void routes(Route... routes) {
		routes(routes[0].getOrigin(), routes[0].getDestination(), LocalTime.parse("08:00"), routes);
	}

	protected void routes(Location from, Location to, LocalTime time, Route... routes) {
		for (Route route : routes) {
			Mockito.when(directionsService.findRoutes(Mockito.eq(from), Mockito.eq(to), Mockito.eq(route.getTravelMode()), Mockito.eq(time)))
					.thenReturn(Sets.newHashSet(route));
		}
	}

	protected Route route(Length distance, Duration duration, TravelMode travelMode) {
		return route(SOUDERTON, ROCKVALE, distance, duration, travelMode);
	}

	protected Route route(Location from, Location to, Length distance, Duration duration, TravelMode travelMode) {
		return Route.from(from).to(to)
				.distance(distance)
				.duration(duration)
				.travelMode(travelMode)
				.build();
	}

	protected Route route(Location from, Location to, Length distance, Duration duration, VehicleType vehicle) {
		return route(from, to, distance, duration, vehicle, LocalTime.parse("08:00"));
	}

	protected Route route(Location from, Location to, Length distance, Duration duration, VehicleType vehicle, LocalTime arrivalTime) {
		Route route = route(from, to, distance, duration, TravelMode.PUBLIC_TRANSIT);
		Transit transit = new Transit();
		LocalTime departureTime = arrivalTime.minus(duration.toPeriod());
		transit.setDepartureTime(departureTime);
		transit.setArrivalTime(arrivalTime);
		transit.setHeadsign(to.getDescription());
		transit.setVehicleType(vehicle);
		route.setTransit(transit);
		return route;
	}

	protected Route route(Route... subroutes) {
		return Route.compose(subroutes).build();

	}

	protected void weather(WeatherCondition weatherCondition) {
		Mockito.when(weatherService.getDailyForecastFor(SOUDERTON))
				.thenReturn(Lists.newArrayList(weatherCondition));
	}

	protected void assertScore(SustainifyUser user, Route route, int value) {
		ScoredRoute scoredRoute = scoreService.scoreFor(route, user);
		assertEquals(value, scoredRoute.getScore());
	}

	protected void assertPointsAwarded(SustainifyUser user, int points) {
		List<ScoredRoute> routes = routeChoiceService.findCoworkerRoutes(user);
		assertEquals(1, routes.size());
		assertEquals(points, routes.get(0).getScore());
	}

	protected void assertRouteChosen(SustainifyUser user, Route route) {
		List<ScoredRoute> routes = routeChoiceService.findCoworkerRoutes(user);
		assertEquals(1, routes.size());
		assertEquals(route, routes.get(0).getRoute());
	}

	protected void chooseRoute(SustainifyUser user, Route route) {
		List<Route> routes = routeService.findRoutesFor(user, ORGANISATION_LOCATION);
		for (Route r : routes) {
			if (r.getTravelMode().equals(route.getTravelMode()) && r.getDistance().equals(route.getDistance())) {
				ScoredRoute scoredRoute = scoreService.scoreFor(route, user);
				routeChoiceService.chooseRoute(scoredRoute);
			}
		}
	}

	protected void assertLeaderboard(int rank, SustainifyUser user, int score) {
		List<UserScore> scores = routeChoiceService.getLeaderboard(APPIFY);
		Assert.assertTrue("Expected at least " + rank + " scores.", scores.size() >= rank);

		UserScore userScore = scores.get(rank - 1);
		Assert.assertEquals(user, userScore.getUser());
		Assert.assertEquals(score, userScore.getScore());
	}

}
