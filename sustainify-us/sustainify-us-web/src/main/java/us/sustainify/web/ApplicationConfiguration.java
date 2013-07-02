package us.sustainify.web;

import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.desirability.DesirabilityScore;
import us.sustainify.commute.domain.model.desirability.WeatherScore;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.score.RouteScore;
import us.sustainify.commute.domain.repository.PersistentRouteRepository;
import us.sustainify.commute.domain.repository.PersistentScoredRouteRepository;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.commute.domain.service.DefaultRouteChoiceService;
import us.sustainify.commute.domain.service.DefaultRouteService;
import us.sustainify.commute.domain.service.DefaultScoreService;
import us.sustainify.commute.domain.service.RouteChoiceService;
import us.sustainify.commute.domain.service.RouteService;
import us.sustainify.commute.domain.service.ScoreService;
import us.sustainify.commute.domain.service.google.directions.GoogleDirectionsService;
import us.sustainify.web.filter.AuthenticationFilter;
import be.appify.framework.cache.Cache;
import be.appify.framework.cache.simple.HashMapCache;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.common.security.service.Sha1EncryptionService;
import be.appify.framework.common.security.service.SimpleAuthenticationService;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.location.service.google.GoogleLocationService;
import be.appify.framework.persistence.Persistence;
import be.appify.framework.persistence.jpa.JPAPersistence;
import be.appify.framework.quantity.Speed;
import be.appify.framework.quantity.Temperature;
import be.appify.framework.security.repository.AuthenticationRepository;
import be.appify.framework.security.repository.PersistentAuthenticationRepository;
import be.appify.framework.security.repository.PersistentUserRepository;
import be.appify.framework.security.repository.UserRepository;
import be.appify.framework.security.service.AuthenticationService;
import be.appify.framework.security.service.EncryptionService;
import be.appify.framework.weather.service.wunderground.WundergroundWeatherService;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;

public class ApplicationConfiguration extends GuiceServletContextListener {
	private static final Reflections STATIC_REFLECTIONS = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage("static"))
			.addScanners(new ResourcesScanner()));

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				filter("/authenticated/*").through(AuthenticationFilter.class);
			}
		}, new SitebricksModule() {
			@Override
			protected void configureSitebricks() {
				scan(HomeServiceProvider.class.getPackage());
				exportAll(this, ".*\\.(js|json|css)");
			}
		}, new AbstractModule() {

			@Override
			protected void configure() {
				Cache cache = new HashMapCache();
				bind(Cache.class).toInstance(cache);
				EntityManagerFactory entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("sustainify-us");
				Persistence persistence = new JPAPersistence(entityManagerFactory);
				bind(Persistence.class).toInstance(persistence);

				GoogleLocationService locationService = new GoogleLocationService(new ApacheHttpTransport(), "AIzaSyCvvR7jLVHXJit5p0hmtuQlMfu6PPt1EtQ");
				locationService.setRetryCount(5);
				bind(LocationService.class).toInstance(locationService);
				UserRepository<SustainifyUser> userRepository = new PersistentUserRepository<SustainifyUser>(persistence, SustainifyUser.class);
				Sha1EncryptionService encryptionService = new Sha1EncryptionService("imagine512", "UTF-8");

				bind(new TypeLiteral<UserRepository<SustainifyUser>>() {
				}).toInstance(userRepository);
				AuthenticationRepository<SustainifyUser> authenticationRepository = new PersistentAuthenticationRepository<>(persistence);
				bind(AuthenticationRepository.class).toInstance(authenticationRepository);
				bind(ScoredRouteRepository.class).to(PersistentScoredRouteRepository.class);
				bind(EncryptionService.class).toInstance(encryptionService);
				bind(RouteChoiceService.class).to(DefaultRouteChoiceService.class);

				AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService = new SimpleAuthenticationService<>(
						userRepository, authenticationRepository);
				bind(new TypeLiteral<AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>>>() {
				}).toInstance(authenticationService);
				HttpTransport transport = new ApacheHttpTransport();

				GoogleDirectionsService directionsService = new GoogleDirectionsService(transport, new PersistentRouteRepository(persistence), cache);
				directionsService.setRetryCount(5);

				WundergroundWeatherService weatherService = new WundergroundWeatherService(transport, "f008e7c5ca6242b5");
				weatherService.setRetryCount(5);
				WeatherScore weatherScore = WeatherScore.withService(weatherService).base(100)
						.subtract(1).perDegreeBelow(Temperature.degreesCelcius(15))
						.subtract(1).perDegreeAbove(Temperature.degreesCelcius(25))
						.subtract(10).perMillimeterPrecipitation()
						.subtract(1).perKilometerPerHourWindSpeedAbove(Speed.kilometersPerHour(10));
				RouteService routeService = new DefaultRouteService(directionsService,
						DesirabilityScore.forTravelMode(TravelMode.BICYCLING).base(0).subtract(1).perMinute().addWeatherScore(weatherScore),
						DesirabilityScore.forTravelMode(TravelMode.WALKING).base(20).subtract(1).perMinute().addWeatherScore(weatherScore),
						DesirabilityScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).base(80).subtract(1).perMinute(),
						DesirabilityScore.forTravelMode(TravelMode.CAR).base(40).subtract(1).perMinute());
				bind(RouteService.class).toInstance(routeService);

				ScoreService scoreService = new DefaultScoreService(
						RouteScore.forTravelMode(TravelMode.BICYCLING).perKilometer(10).addWeatherScore(weatherScore),
						RouteScore.forTravelMode(TravelMode.WALKING).perKilometer(10).addWeatherScore(weatherScore),
						RouteScore.forTravelMode(TravelMode.PUBLIC_TRANSIT).perKilometer(5),
						RouteScore.forTravelMode(TravelMode.CAR).perKilometer(1));
				bind(ScoreService.class).toInstance(scoreService);
			}
		});
	}

	protected void exportAll(SitebricksModule sitebricksModule, String pattern) {
		Set<String> staticResources = STATIC_REFLECTIONS.getResources(Pattern.compile(pattern));
		for (String resource : staticResources) {
			String base = resource.substring(7);
			sitebricksModule.at("/" + base).export("/" + resource);
		}
	}
}
