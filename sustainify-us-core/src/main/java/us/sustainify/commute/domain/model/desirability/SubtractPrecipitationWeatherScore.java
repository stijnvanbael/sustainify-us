package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.weather.domain.WeatherCondition;

public class SubtractPrecipitationWeatherScore extends WeatherScore {
	private final WeatherScore weatherScore;
	private final int score;

	public SubtractPrecipitationWeatherScore(WeatherScore weatherScore, int score) {
		super(weatherScore.getWeatherService(), weatherScore.getTimestampService());
		this.weatherScore = weatherScore;
		this.score = score;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		int penalty = score * (int) currentCondition.getPrecipitation().getMillimeters();
		return weatherScore.getScoreInternal(currentCondition) - penalty;
	}

}
