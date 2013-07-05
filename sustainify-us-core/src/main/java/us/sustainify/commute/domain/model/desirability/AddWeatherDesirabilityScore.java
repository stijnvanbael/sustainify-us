package us.sustainify.commute.domain.model.desirability;

import us.sustainify.commute.domain.model.route.Route;

public class AddWeatherDesirabilityScore extends DesirabilityScore {

	private final DesirabilityScore desirabilityScore;
	private final WeatherScore weatherScore;

	public AddWeatherDesirabilityScore(DesirabilityScore desirabilityScore, WeatherScore weatherScore) {
		super(desirabilityScore.getTravelMode());
		this.desirabilityScore = desirabilityScore;
		this.weatherScore = weatherScore;
	}

	@Override
	protected int forRouteInternal(Route route) {
		return desirabilityScore.forRoute(route) + weatherScore.scoreFor(route.getOrigin());
	}

}
