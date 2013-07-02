package us.sustainify.web.authenticated.widget;

import static us.sustainify.web.authenticated.widget.TravelModes.ROUTE_DESCRIPTIONS;
import static us.sustainify.web.authenticated.widget.TravelModes.SUBROUTE_DESCRIPTIONS;
import static us.sustainify.web.authenticated.widget.TravelModes.TRAVEL_MODES;
import static us.sustainify.web.authenticated.widget.TravelModes.TRAVEL_MODE_IDS;

import java.util.List;
import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.TravelModeSummary;

import com.google.common.collect.Lists;
import com.google.sitebricks.Show;
import com.google.sitebricks.rendering.EmbedAs;

@EmbedAs("Routes")
@Show("Routes.html")
public class RoutesWidget {
	private static final ReadableDuration ONE_HOUR = Duration.standardHours(1);
	public static final ReadableDuration ONE_MINUTE = Duration.standardMinutes(1);
	public static final ReadableDuration FIVE_MINUTES = Duration.standardMinutes(5);

	private List<ScoredRoute> routes;
	private ScoredRoute chosenRoute;
	private String destination;

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setRoutes(List<ScoredRoute> routes) {
		this.routes = routes;
	}

	public void setChosenRoute(ScoredRoute chosenRoute) {
		this.chosenRoute = chosenRoute;
	}

	public List<RouteWidget> getRoutes() {
		List<RouteWidget> routes = Lists.newArrayList();
		for (ScoredRoute route : this.routes) {
			routes.add(new RouteWidget(route));
		}
		return routes;
	}

	public class RouteWidget {
		private final ScoredRoute route;
		private final List<SubrouteWidget> subroutes;

		public RouteWidget(ScoredRoute route) {
			this.route = route;
			this.subroutes = Lists.newArrayList();
			for (Route subroute : route.getRoute().getSubroutes()) {
				subroutes.add(new SubrouteWidget(subroute));
			}
		}

		public List<SubrouteWidget> getSubroutes() {
			return subroutes;
		}

		public String getId() {
			return TRAVEL_MODE_IDS.get(route.getRoute().getTravelMode());
		}

		public int getScore() {
			return route.getScore();
		}

		public String getDescription() {
			return ROUTE_DESCRIPTIONS.get(route.getRoute().getTravelMode()) + " (" + format(route.getRoute().getDuration()) + ")";
		}

		public String getWorkLocation() {
			return destination;
		}

		public List<RouteSummaryWidget> getSummary() {
			List<RouteSummaryWidget> summary = Lists.newArrayList();
			Map<TravelMode, TravelModeSummary> routeSummary = route.getRoute().getSummary();
			for (TravelMode travelMode : TravelMode.values()) {
				if (routeSummary.keySet().contains(travelMode)) {
					summary.add(new RouteSummaryWidget(travelMode, routeSummary.get(travelMode)));
				}
			}
			return summary;
		}

		public boolean isChosen() {
			return chosenRoute != null && route.getRoute().getCode().equals(chosenRoute.getRoute().getCode());
		}

		private String format(Duration duration) {
			StringBuilder builder = new StringBuilder();
			boolean moreThanFiveMinutes = duration.isLongerThan(FIVE_MINUTES);
			if (duration.isLongerThan(ONE_HOUR)) {
				builder.append(duration.getStandardHours()).append(" h");
				duration = duration.minus(Duration.standardHours(duration.getStandardHours()));
			}
			if (duration.isLongerThan(ONE_MINUTE)) {
				if (builder.length() > 0) {
					builder.append(" ");
				}
				builder.append(duration.getStandardMinutes()).append(" min");
				duration = duration.minus(Duration.standardMinutes(duration.getStandardMinutes()));
			}
			if (!moreThanFiveMinutes) {
				if (builder.length() > 0) {
					builder.append(" ");
				}
				builder.append(duration.getStandardSeconds());
				builder.append(" s");
			}
			return builder.toString();
		}
	}

	public static class SubrouteWidget {
		private final Route subroute;

		public SubrouteWidget(Route subroute) {
			this.subroute = subroute;
		}

		public String getTravelMode() {
			return TRAVEL_MODES.get(subroute.getTravelMode());
		}

		public String getOrigin() {
			return subroute.getOrigin().getLatitude() + "," + subroute.getOrigin().getLongitude();
		}

		public String getDestination() {
			return subroute.getDestination().getLatitude() + "," + subroute.getDestination().getLongitude();
		}

		public String getArrivalTime() {
			return subroute.getTransit() != null ? Integer.toString(subroute.getTransit().getArrivalTime().getMillisOfDay()) : "";
		}
	}

	public static class RouteSummaryWidget {
		private final TravelMode travelMode;
		private final TravelModeSummary travelModeSummary;

		public RouteSummaryWidget(TravelMode travelMode, TravelModeSummary travelModeSummary) {
			this.travelMode = travelMode;
			this.travelModeSummary = travelModeSummary;
		}

		public String getId() {
			return TRAVEL_MODE_IDS.get(travelMode);
		}

		public String getDescription() {
			return SUBROUTE_DESCRIPTIONS.get(travelMode);
		}

		public String getDistance() {
			return travelModeSummary.getDistance().toString();
		}

	}

}
