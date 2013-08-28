package us.sustainify.commute.domain.model.score;

import us.sustainify.commute.domain.model.desirability.WeatherScore;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;

public abstract class RouteScore {

	public class MultiplyByWeatherRouteScore extends RouteScore {

		private final RouteScore routeScore;
		private final WeatherScore weatherScore;

		public MultiplyByWeatherRouteScore(RouteScore routeScore, WeatherScore weatherScore) {
			super(routeScore.travelMode);
			this.routeScore = routeScore;
			this.weatherScore = weatherScore;
		}

		@Override
		protected int forRouteInternal(Route route) {
			return (int) (routeScore.forRoute(route) * (100d / weatherScore.scoreFor(route.getOrigin())));
		}

	}

	private final TravelMode travelMode;

	public static class PerKilometerRouteScore extends RouteScore {
		private final int scorePerKilometer;

		public PerKilometerRouteScore(TravelMode travelMode, int scorePerKilometer) {
			super(travelMode);
			this.scorePerKilometer = scorePerKilometer;
		}

		@Override
		protected int forRouteInternal(Route route) {
			return (int) (route.getDistance().getKilometers() * scorePerKilometer);
		}

	}

	public RouteScore(TravelMode travelMode) {
		this.travelMode = travelMode;
	}

	public int forRoute(Route route) {
		if (route.getTravelMode() == travelMode) {
			return forRouteInternal(route);
		}
		return 0;
	}

	protected abstract int forRouteInternal(Route route);

	public static RouteScoreBuilder forTravelMode(TravelMode travelMode) {
		return new RouteScoreBuilder(travelMode);
	}

	public static class RouteScoreBuilder {
		private final TravelMode travelMode;

		public RouteScoreBuilder(TravelMode travelMode) {
			this.travelMode = travelMode;
		}

		public RouteScore perKilometer(int scorePerKilometer) {
			return new PerKilometerRouteScore(travelMode, scorePerKilometer);
		}

	}

	public RouteScore addWeatherScore(WeatherScore weatherScore) {
		return new MultiplyByWeatherRouteScore(this, weatherScore);
	}
}
