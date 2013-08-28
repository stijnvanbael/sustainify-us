package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.weather.domain.WeatherCondition;
import be.appify.framework.weather.service.WeatherService;
import us.sustainify.common.domain.service.system.TimestampService;

public class FixedWeatherScore extends WeatherScore {

	private final int score;

	public FixedWeatherScore(WeatherService weatherService, TimestampService timestampService, int score) {
		super(weatherService, timestampService);
		this.score = score;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		return score;
	}

}
