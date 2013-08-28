package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.quantity.Temperature;
import be.appify.framework.weather.domain.WeatherCondition;

public class SubtractHotWeatherScore extends WeatherScore {

	private final WeatherScore weatherScore;
	private final Temperature temperature;
	private final int score;

	public SubtractHotWeatherScore(WeatherScore weatherScore, int score, Temperature temperature) {
		super(weatherScore.getWeatherService(), weatherScore.getTimestampService());
		this.weatherScore = weatherScore;
		this.score = score;
		this.temperature = temperature;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		int penalty = score * (int) (Math.max(
				currentCondition.getMaxTemperature().getDegreesCelcius(),
				temperature.getDegreesCelcius()) - temperature.getDegreesCelcius());
		return weatherScore.getScoreInternal(currentCondition) - penalty;
	}

}
