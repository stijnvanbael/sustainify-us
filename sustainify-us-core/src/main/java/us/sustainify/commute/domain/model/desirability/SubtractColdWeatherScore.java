package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.quantity.Temperature;
import be.appify.framework.weather.domain.WeatherCondition;

public class SubtractColdWeatherScore extends WeatherScore {

	private final WeatherScore weatherScore;
	private final Temperature temperature;
	private final int score;

	public SubtractColdWeatherScore(WeatherScore weatherScore, int score, Temperature temperature) {
		super(weatherScore.getWeatherService());
		this.weatherScore = weatherScore;
		this.score = score;
		this.temperature = temperature;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		int penalty = score * (int) (temperature.getDegreesCelcius() - Math.min(
				currentCondition.getMinTemperature().getDegreesCelcius(),
				temperature.getDegreesCelcius()));
		return weatherScore.getScoreInternal(currentCondition) - penalty;
	}

}
