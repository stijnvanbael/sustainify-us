package us.sustainify.commute.domain.model.desirability;

import java.util.List;

import org.joda.time.Instant;
import org.slf4j.*;

import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.*;
import be.appify.framework.weather.domain.WeatherCondition;
import be.appify.framework.weather.service.WeatherService;
import us.sustainify.common.domain.service.system.TimestampService;

public abstract class WeatherScore {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherScore.class);

    public TimestampService getTimestampService() {
        return timestampService;
    }

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
    private final TimestampService timestampService;

    public WeatherScore(WeatherService weatherService, TimestampService timestampService) {
		this.weatherService = weatherService;
        this.timestampService = timestampService;
    }

	public static Builder withServices(WeatherService weatherService, TimestampService timestampService) {
		return new Builder(weatherService, timestampService);
	}

	public static class Builder {

		private final WeatherService weatherService;
        private TimestampService timestampService;

        public Builder(WeatherService weatherService, TimestampService timestampService) {
			this.weatherService = weatherService;
            this.timestampService = timestampService;
        }

		public WeatherScore base(int score) {
			return new FixedWeatherScore(weatherService, timestampService, score);
		}

	}

	public int scoreFor(Location location) {
		try {
			List<WeatherCondition> conditions = weatherService.getDailyForecastFor(location);
			WeatherCondition currentCondition = findCurrentCondition(conditions);
			if (currentCondition == null) {
				return 100;
			}
            int score = getScoreInternal(currentCondition);
            return score < 1 ? 1 : score;
		} catch (Exception e) {
			LOGGER.warn("Unable to determine weather score for " + location, e);
		}
		return 100;
	}

	protected abstract int getScoreInternal(WeatherCondition currentCondition);

	private WeatherCondition findCurrentCondition(List<WeatherCondition> conditions) {
		for (WeatherCondition condition : conditions) {
			if (condition.getValidity().contains(timestampService.getCurrentTimestamp().toDateTime())) {
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
