package us.sustainify.commute.components;

import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.common.security.service.Sha1EncryptionService;
import be.appify.framework.common.security.service.SimpleAuthenticationService;
import be.appify.framework.location.domain.Location;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.persistence.Persistence;
import be.appify.framework.persistence.Transaction;
import be.appify.framework.persistence.jpa.JPAPersistence;
import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Speed;
import be.appify.framework.quantity.Temperature;
import be.appify.framework.security.repository.AuthenticationRepository;
import be.appify.framework.security.repository.PersistentAuthenticationRepository;
import be.appify.framework.security.repository.PersistentUserRepository;
import be.appify.framework.security.repository.UserRepository;
import be.appify.framework.security.service.AuthenticationService;
import be.appify.framework.security.service.EncryptionService;
import be.appify.framework.weather.service.WeatherService;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.mockito.Mockito;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.common.domain.repository.organisation.OrganisationRepository;
import us.sustainify.common.domain.repository.organisation.PersistentOrganisationRepository;
import us.sustainify.common.domain.repository.system.PersistentSystemSettingsRepository;
import us.sustainify.common.domain.repository.system.SystemSettingsRepository;
import us.sustainify.common.domain.service.system.TimestampService;
import us.sustainify.commute.domain.model.desirability.DesirabilityScore;
import us.sustainify.commute.domain.model.desirability.WeatherScore;
import us.sustainify.commute.domain.model.route.*;
import us.sustainify.commute.domain.model.score.RouteScore;
import us.sustainify.commute.domain.model.statistics.Aggregation;
import us.sustainify.commute.domain.model.statistics.Statistics;
import us.sustainify.commute.domain.repository.PersistentScoredRouteRepository;
import us.sustainify.commute.domain.repository.PersistentStatisticsRepository;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.commute.domain.repository.StatisticsRepository;
import us.sustainify.commute.domain.service.*;

import javax.annotation.Nullable;
import javax.persistence.EntityManagerFactory;
import java.util.UUID;

public class TestSystem {
    public static final LocalDate START = LocalDate.parse("2013-07-01");
    public static final LocalDate END = LocalDate.parse("2013-07-31");

    private final EncryptionService encryptionService;
    private final AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService;
    private final RouteService routeService;
    private final ScoreService scoreService;
    private final OrganisationRepository organisationRepository;
    private final RouteChoiceService routeChoiceService;
    private final UserRepository<SustainifyUser> userRepository;
    private final WeatherService weatherService;
    private final DirectionsService directionsService;
    private final LocationService locationService;
    private final StatisticsRepository statisticsRepository;
    private final SystemSettings systemSettings;

    public TestSystem() {
        EntityManagerFactory entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("sustainify-us");
        Persistence persistence = new JPAPersistence(entityManagerFactory);

        truncateData(persistence);

        TimestampService timestampService = Mockito.mock(TimestampService.class);
        Mockito.when(timestampService.getCurrentDate()).thenReturn(LocalDate.parse("2013-08-01"));
        Mockito.when(timestampService.getCurrentTimestamp()).thenReturn(LocalDateTime.parse("2013-08-01T12:00"));
        Mockito.when(timestampService.getCurrentTime()).thenReturn(LocalTime.parse("12:00"));

        SystemSettingsRepository systemSettingsRepository = new PersistentSystemSettingsRepository(persistence);
        systemSettings = systemSettingsRepository.getSystemSettings();

        locationService = Mockito.mock(LocationService.class);
        userRepository = new PersistentUserRepository<>(persistence, SustainifyUser.class);
        organisationRepository = new PersistentOrganisationRepository(persistence);
        this.encryptionService = new Sha1EncryptionService("", "UTF-8");

        AuthenticationRepository<SustainifyUser> authenticationRepository = new PersistentAuthenticationRepository<>(persistence);
        ScoredRouteRepository scoredRouteRepository = new PersistentScoredRouteRepository(persistence, timestampService);
        routeChoiceService = new DefaultRouteChoiceService(scoredRouteRepository);

        authenticationService = new SimpleAuthenticationService<>(userRepository, authenticationRepository);

        directionsService = Mockito.mock(DirectionsService.class);
        weatherService = Mockito.mock(WeatherService.class);

        WeatherScore weatherScore = WeatherScore.withServices(weatherService, timestampService).base(100)
                .subtract(1).perDegreeBelow(Temperature.degreesCelcius(15))
                .subtract(1).perDegreeAbove(Temperature.degreesCelcius(25))
                .subtract(5).perMillimeterPrecipitation()
                .subtract(1).perKilometerPerHourWindSpeedAbove(Speed.kilometersPerHour(10));
        routeService = new DefaultRouteService(directionsService, timestampService,
                DesirabilityScore.forTravelMode(TravelMode.BICYCLING).base(0).subtract(1).perMinute().addWeatherScore(weatherScore),
                DesirabilityScore.forTravelMode(TravelMode.WALKING).base(20).subtract(1).perMinute().addWeatherScore(weatherScore),
                DesirabilityScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).base(80).subtract(1).perMinute(),
                DesirabilityScore.forTravelMode(TravelMode.CAR).base(40).subtract(1).perMinute());

        scoreService = new DefaultScoreService(
                RouteScore.forTravelMode(TravelMode.BICYCLING).perKilometer(10).addWeatherScore(weatherScore),
                RouteScore.forTravelMode(TravelMode.WALKING).perKilometer(10).addWeatherScore(weatherScore),
                RouteScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).perKilometer(5),
                RouteScore.forTravelMode(TravelMode.CAR).perKilometer(1));

        statisticsRepository = new PersistentStatisticsRepository(persistence, systemSettings);
    }

    private void truncateData(Persistence persistence) {
        Transaction transaction = persistence.beginTransaction();
        transaction.execute("TRUNCATE SCHEMA public AND COMMIT");
        transaction.commit();
    }

    public TestOrganisation organisation(String name) {
        TestOrganisation organisation = new TestOrganisation(name, this);
        organisationRepository.store(organisation.getOrganisation());
        return organisation;
    }

    public void store(SustainifyUser user) {
        userRepository.store(user);
    }

    public void defineRoute(SustainifyUser user, TravelMode travelMode, VehicleType vehicleType, Length distance) {
        Location origin = user.getPreferences().getHomeLocation();
        Location destination = user.getOrganisation().getLocations().get(0).getLocation();
        LocalTime arrival = user.getPreferences().getOfficeHours().get(0).getArrival();
        Route route = Route.from(origin)
                .to(destination)
                .code(UUID.randomUUID().toString())
                .distance(distance)
                .duration(Duration.standardMinutes((long) (distance.getKilometers() * modifier(travelMode))))
                .travelMode(travelMode)
                .build();
        if(vehicleType != null) {
            Transit transit = new Transit();
            transit.setVehicleType(vehicleType);
            transit.setDepartureTime(arrival.minusMinutes(30));
            transit.setArrivalTime(arrival.minusMinutes(5));
            transit.setHeadsign("1 " + destination.getName());
            route.setTransit(transit);
        }
        Mockito.when(directionsService.findRoutes(origin, destination, travelMode, arrival)).thenReturn(Sets.newHashSet(route));
    }

    private double modifier(TravelMode travelMode) {
        switch (travelMode) {
            case BICYCLING:
                return 3.5;
            case PUBLIC_TRANSIT:
                return 2.5;
            case CAR:
                return 1.0;
            case WALKING:
                return 15.0;
        }
        return 0.0;
    }

    public void confirmRoute(SustainifyUser user, final TravelMode travelMode, final VehicleType vehicleType, LocalDate date) {
        Route route = Collections2.filter(routeService.findRoutesFor(user, user.getPreferences().getDefaultLocation()), new Predicate<Route>() {
            @Override
            public boolean apply(Route input) {
                Transit transit = input.getSubroutes().get(0).getTransit();
                return input.getTravelMode() == travelMode && (vehicleType == null || (transit != null && transit.getVehicleType() == vehicleType));
            }
        }).iterator().next();
        ScoredRoute scoredRoute = scoreService.scoreFor(route, user);
        scoredRoute.setDay(date);
        routeChoiceService.chooseRoute(scoredRoute);
    }

    public TestStatistics getStatisticsFor(TestUser user) {
        Statistics statistics = statisticsRepository.getStatisticsFor(user.getUser(), START, END, Aggregation.MONTH);
        return new TestStatistics(statistics);
    }

    public TestSystemSettings settings() {
        return new TestSystemSettings(systemSettings);
    }
}
