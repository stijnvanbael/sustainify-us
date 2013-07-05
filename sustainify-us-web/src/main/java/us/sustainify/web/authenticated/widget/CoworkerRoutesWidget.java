package us.sustainify.web.authenticated.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import be.appify.framework.location.domain.Location;
import be.appify.framework.security.domain.User;

import com.google.common.collect.Lists;
import com.google.sitebricks.Show;
import com.google.sitebricks.rendering.EmbedAs;

@EmbedAs("CoworkerRoutes")
@Show("CoworkerRoutes.html")
public class CoworkerRoutesWidget {
	private List<ScoredRoute> coworkerRoutes;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMMM d", Locale.ENGLISH);

	public void setCoworkerRoutes(List<ScoredRoute> coworkerRoutes) {
		this.coworkerRoutes = coworkerRoutes;
	}

	public List<CoworkerRouteWidget> getCoworkerRoutes() {
		List<CoworkerRouteWidget> coworkerRoutes = Lists.newArrayList();
		for (ScoredRoute scoredRoute : this.coworkerRoutes) {
			coworkerRoutes.add(new CoworkerRouteWidget(scoredRoute));
		}
		return coworkerRoutes;
	}

	public static class CoworkerRouteWidget {

		private final ScoredRoute scoredRoute;

		public CoworkerRouteWidget(ScoredRoute scoredRoute) {
			this.scoredRoute = scoredRoute;
		}

		public String getName() {
			User user = scoredRoute.getUser();
			return user.getFirstName() + " " + user.getLastName();
		}

		public String getDestination() {
			SustainifyUser user = scoredRoute.getUser();
			Location destination = scoredRoute.getRoute().getDestination();
			for (OrganisationLocation location : user.getOrganisation().getLocations()) {
				if (Math.abs(location.getLocation().getLatitude() - destination.getLatitude()) < 0.02
						&& Math.abs(location.getLocation().getLongitude() - destination.getLongitude()) < 0.02) {
					return location.getName();
				}

			}
			return destination.getName();
		}

		public String getDay() {
			return DATE_FORMAT.format(scoredRoute.getDay().toDate());
		}

		public String getTravelMode() {
			return TravelModes.ROUTE_DESCRIPTIONS.get(scoredRoute.getRoute().getTravelMode());
		}

		public int getScore() {
			return scoredRoute.getScore();
		}
	}
}
