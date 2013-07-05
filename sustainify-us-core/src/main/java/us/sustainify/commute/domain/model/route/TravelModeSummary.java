package us.sustainify.commute.domain.model.route;

import org.joda.time.Duration;

import be.appify.framework.quantity.Length;

public class TravelModeSummary {
	private final Length distance;
	private final Duration duration;

	TravelModeSummary(Length distance, Duration duration) {
		this.distance = distance;
		this.duration = duration;
	}

	public Length getDistance() {
		return distance;
	}

	public Duration getDuration() {
		return duration;
	}
}
