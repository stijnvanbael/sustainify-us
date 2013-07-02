package us.sustainify.commute.domain.model.route;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.Duration;

import be.appify.framework.domain.AbstractEntity;
import be.appify.framework.domain.ReflectionBuilder;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Entity
public class Route extends AbstractEntity {
	private static final long serialVersionUID = 2024189427448153722L;
	private static final NumberFormat TIME_FORMAT = new DecimalFormat("00");

	@Type(type = "be.appify.framework.persistence.jpa.LengthType")
	@Column(nullable = false)
	private Length distance;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDurationAsString")
	@Column(nullable = false)
	private Duration duration;

	@Enumerated(EnumType.STRING)
	@Column(name = "travel_mode", nullable = false)
	private TravelMode travelMode;

	@Column(length = 4000, nullable = false)
	private String code;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "origin_name")),
			@AttributeOverride(name = "latitude", column = @Column(name = "origin_latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "origin_longitude"))
	})
	private Location origin;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "destination_name")),
			@AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude"))
	})
	private Location destination;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinColumn(name = "parent_route_id")
	private List<Route> subroutes;

	@Embedded
	private Transit transit;

	@Transient
	private Map<TravelMode, TravelModeSummary> summary;

	Route() {
	}

	private void initialize() {
		summary = Maps.newHashMap();
		if (!subroutes.isEmpty()) {
			Map<TravelMode, Length> distances = Maps.newHashMap();
			Map<TravelMode, Duration> durations = Maps.newHashMap();
			for (Route subroute : subroutes.size() == 1 && !subroutes.get(0).getSubroutes().isEmpty() ? subroutes.get(0).getSubroutes() : subroutes) {
				addDistance(distances, subroute);
				addDuration(durations, subroute);
			}
			if (origin == null) {
				origin = subroutes.get(0).getOrigin();
			}
			if (destination == null) {
				destination = subroutes.get(subroutes.size() - 1).getDestination();
			}
			if (travelMode == null) {
				determineTravelMode(distances);
			}
			if (distance == null) {
				determineDistance(distances);
			}
			if (duration == null) {
				determineDuration(durations);
			}
			fillSummary(distances, durations);
		} else {
			summary.put(travelMode, new TravelModeSummary(distance, duration));
		}
	}

	private void determineDuration(Map<TravelMode, Duration> durations) {
		Duration duration = Duration.millis(0);
		for (Duration d : durations.values()) {
			duration = duration.plus(d);
		}
		this.duration = duration;
	}

	private void determineDistance(Map<TravelMode, Length> distances) {
		Length distance = Length.millimeters(0);
		for (Length d : distances.values()) {
			distance = distance.add(d);
		}
		this.distance = distance;
	}

	private void fillSummary(Map<TravelMode, Length> distances, Map<TravelMode, Duration> durations) {
		for (TravelMode travelMode : distances.keySet()) {
			Length distance = distances.get(travelMode);
			Duration duration = durations.get(travelMode);
			summary.put(travelMode, new TravelModeSummary(distance, duration));
		}
	}

	private void addDuration(Map<TravelMode, Duration> durations, Route subroute) {
		TravelMode travelMode = subroute.getTravelMode();
		Duration totalDuration = durations.get(travelMode);
		if (totalDuration == null) {
			totalDuration = Duration.standardSeconds(0);
		}
		totalDuration = totalDuration.plus(subroute.getDuration());
		durations.put(travelMode, totalDuration);
	}

	private void addDistance(Map<TravelMode, Length> distances, Route subroute) {
		TravelMode travelMode = subroute.getTravelMode();
		Length totalDistance = distances.get(travelMode);
		if (totalDistance == null) {
			totalDistance = Length.meters(0);
		}
		totalDistance = totalDistance.add(subroute.getDistance());
		distances.put(travelMode, totalDistance);
	}

	private void determineTravelMode(Map<TravelMode, Length> distances) {
		TravelMode longestTravelMode = null;
		Length longest = Length.meters(0);
		for (TravelMode travelMode : distances.keySet()) {
			Length distance = distances.get(travelMode);
			if (distance.compareTo(longest) > 0) {
				longest = distance;
				longestTravelMode = travelMode;
			}
		}
		travelMode = longestTravelMode;
	}

	public Length getDistance() {
		return distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public TravelMode getTravelMode() {
		return travelMode;
	}

	public Location getOrigin() {
		return origin;
	}

	public Location getDestination() {
		return destination;
	}

	public String getCode() {
		return code;
	}

	public List<Route> getSubroutes() {
		return Collections.unmodifiableList(subroutes);
	}

	public static Builder from(Location origin) {
		return new Builder().from(origin);
	}

	public static Builder compose(Route... subroutes) {
		return compose(Lists.newArrayList(subroutes));
	}

	public static Builder compose(List<Route> subroutes) {
		return new Builder().subroutes(subroutes);
	}

	public static class Builder extends ReflectionBuilder<Route, Builder> {
		private Builder() {
			super(Route.class);
			set("subroutes", Lists.newArrayList());
		}

		public Builder travelMode(TravelMode travelMode) {
			return set("travelMode", travelMode);
		}

		public Builder subroutes(@NotNull List<Route> subroutes) {
			return set("subroutes", Lists.newArrayList(subroutes));
		}

		public Builder duration(@NotNull Duration duration) {
			return set("duration", duration);
		}

		public Builder origin(@NotNull Location origin) {
			return set("origin", origin);
		}

		public Builder from(Location origin) {
			return origin(origin);
		}

		public Builder to(Location destination) {
			return destination(destination);
		}

		public Builder destination(@NotNull Location destination) {
			return set("destination", destination);
		}

		public Builder distance(@NotNull Length distance) {
			return set("distance", distance);
		}

		public Builder code(@NotNull String code) {
			return set("code", code);
		}

		@Override
		public Route build() {
			List<Route> subroutes = this.<List<Route>> get("subroutes");
			if (get("travelMode") == null && subroutes.isEmpty()) {
				throw new IllegalStateException("Either travelMode or subroutes are required");
			}
			if (get("distance") == null && subroutes.isEmpty()) {
				throw new IllegalStateException("Either distance or subroutes are required");
			}
			if (get("duration") == null && subroutes.isEmpty()) {
				throw new IllegalStateException("Either duration or subroutes are required");
			}
			if (get("origin") == null && subroutes.isEmpty()) {
				throw new IllegalStateException("Either origin or subroutes are required");
			}
			if (get("destination") == null && subroutes.isEmpty()) {
				throw new IllegalStateException("Either destination or subroutes are required");
			}
			if (get("code") == null) {
				set("code", joinCodes(subroutes));
			}
			Route route = super.build();
			route.initialize();
			return route;
		}

		private String joinCodes(List<Route> subroutes) {
			StringBuilder builder = new StringBuilder();
			for (Route route : subroutes) {
				if (builder.length() > 0) {
					builder.append(";");
				}
				builder.append(route.getCode());
			}
			return builder.toString();
		}
	}

	@Override
	public String toString() {
		return travelMode + " (" + distance + ", " + formatDuration() + ")";
	}

	private String formatDuration() {
		StringBuilder builder = new StringBuilder();
		Duration duration = this.duration;
		builder.append(TIME_FORMAT.format(duration.getStandardHours())).append(":");
		duration = duration.minus(Duration.standardHours(duration.getStandardHours()));
		builder.append(TIME_FORMAT.format(duration.getStandardMinutes())).append(":");
		duration = duration.minus(Duration.standardMinutes(duration.getStandardMinutes()));
		builder.append(TIME_FORMAT.format(duration.getStandardSeconds()));
		return builder.toString();
	}

	public Map<TravelMode, TravelModeSummary> getSummary() {
		if (summary == null) {
			initialize();
		}
		return Collections.unmodifiableMap(summary);
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public void setDistance(Length distance) {
		this.distance = distance;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public void setSubroutes(List<Route> subroutes) {
		this.subroutes = subroutes;
	}

	public void setTransit(Transit transit) {
		this.transit = transit;
	}

	public Transit getTransit() {
		return transit;
	}
}
