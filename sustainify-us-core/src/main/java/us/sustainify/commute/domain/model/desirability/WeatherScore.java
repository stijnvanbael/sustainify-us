package us.sustainify.commute.domain.model.desirability;

import java.util.List;

import org.slf4j.*;

import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.*;
import be.appify.framework.weather.domain.WeatherCondition;
import be.appify.framework.weather.service.WeatherService;

public abstract class WeatherScore {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherScore.class);

	public static class SubtractBuilder {

		private final int score;
		private final WeatherScore weatherScore;

		public SubtractBuilder(WeatherScore weatherScore, int score) {
			this.weatherScore = weatherScore;
			this.score = score;
		}

		public WeatherScore perDegreeBelow(Temperature temperature) {
			return new SubtractColdWeatherScore(weatherScore, score, temperature);
		}

		public WeatherScore perDegreeAbove(Temperature temperature) {
			return new SubtractHotWeatherScore(weatherScore, score, temperature);
		}

		public WeatherScore perMillimeterPrecipitation() {
			return new SubtractPrecipitationWeatherScore(weatherScore, score);
		}

		public WeatherScore perKilometerPerHourWindSpeedAbove(Speed minSpeed) {
			return new SubtractWindWeatherScore(weatherScore, score, minSpeed);
		}

	}

	private final WeatherService weatherService;

	public WeatherScore(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	public static Builder withService(WeatherService weatherService) {
		return new Builder(weatherService);
	}

	public static class Builder {

		private final WeatherService weatherService;

		public Builder(WeatherService weatherService) {
			this.weatherService = weatherService;
		}

		public WeatherScore base(int score) {
			return new FixedWeatherScore(weatherService, score);
		}

	}

	public int scoreFor(Location location) {
		try {
			List<WeatherCondition> conditions = weatherService.getDailyForecastFor(location);
			WeatherCondition currentCondition = findCurrentCondition(conditions);
			if (currentCondition == null) {
				return 0;
			}
			return getScoreInternal(currentCondition);
		} catch (Exception e) {
			LOGGER.warn("Unable to determine weather score for " + location, e);
		}
		return 0;
	}

	protected abstract int getScoreInternal(WeatherCondition currentCondition);

	private WeatherCondition findCurrentCondition(List<WeatherCondition> conditions) {
		for (WeatherCondition condition : conditions) {
			if (condition.getValidity().containsNow()) {
				return condition;
			}
		}
		return null;
	}

	public SubtractBuilder subtract(int score) {
		return new SubtractBuilder(this, score);
	}

	public WeatherService getWeatherService() {
		return weatherService;
	}
}
