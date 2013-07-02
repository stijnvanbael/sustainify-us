package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.weather.domain.WeatherCondition;
import be.appify.framework.weather.service.WeatherService;

public class FixedWeatherScore extends WeatherScore {

	private final int score;

	public FixedWeatherScore(WeatherService weatherService, int score) {
		super(weatherService);
		this.score = score;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		return score;
	}

}
