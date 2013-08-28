package us.sustainify.commute.domain.model.desirability;

import be.appify.framework.quantity.Speed;
import be.appify.framework.weather.domain.WeatherCondition;

public class SubtractWindWeatherScore extends WeatherScore {

	private final WeatherScore weatherScore;
	private final int score;
	private final Speed minSpeed;

	public SubtractWindWeatherScore(WeatherScore weatherScore, int score, Speed minSpeed) {
		super(weatherScore.getWeatherService(), weatherScore.getTimestampService());
		this.weatherScore = weatherScore;
		this.score = score;
		this.minSpeed = minSpeed;
	}

	@Override
	protected int getScoreInternal(WeatherCondition currentCondition) {
		int penalty = score * (int) (Math.max(
				currentCondition.getWindSpeed().getKilometersPerHour(),
				minSpeed.getKilometersPerHour()) - minSpeed.getKilometersPerHour());
		return weatherScore.getScoreInternal(currentCondition) - penalty;
	}

}
