package us.sustainify.commute.domain.model.desirability;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;

public abstract class DesirabilityScore {
	private final TravelMode travelMode;

	public DesirabilityScore(TravelMode travelMode) {
		this.travelMode = travelMode;
	}

	public static Builder forTravelMode(TravelMode travelMode) {
		return new Builder(travelMode);
	}

	public SubtractBuilder subtract(int score) {
		return new SubtractBuilder(this, score);
	}

	public DesirabilityScore addWeatherScore(WeatherScore weatherScore) {
		return new AddWeatherDesirabilityScore(this, weatherScore);
	}

	public int forRoute(Route route) {
		if (route.getTravelMode() == travelMode) {
			return forRouteInternal(route);
		}
		return 0;
	}

	public TravelMode getTravelMode() {
		return travelMode;
	}

	protected abstract int forRouteInternal(Route route);

	public static class Builder {

		private final TravelMode travelMode;

		public Builder(TravelMode travelMode) {
			this.travelMode = travelMode;
		}

		public DesirabilityScore base(int score) {
			return new FixedDesirabilityScore(travelMode, score);
		}

	}

	public static class SubtractBuilder {

		private final DesirabilityScore desirabilityScore;
		private final int score;

		public SubtractBuilder(DesirabilityScore desirabilityScore, int score) {
			this.desirabilityScore = desirabilityScore;
			this.score = score;
		}

		public DesirabilityScore perMinute() {
			return new SubtractPerMinuteDesirabilityScore(desirabilityScore, score);
		}
	}

}
