package us.sustainify.web.authenticated;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.service.system.TimestampService;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.commute.domain.service.RouteService;
import us.sustainify.commute.domain.service.ScoreService;
import us.sustainify.web.SessionContext;
import be.appify.framework.security.domain.User;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;

@At("/authenticated/routes")
@Show("Routes.html")
public class RouteSuggestionsPagePart {
	private static final Logger LOGGER = LoggerFactory.getLogger(RouteSuggestionsPage.class);

    private final RouteService routeService;
	private final SessionContext sessionContext;
	private final ScoreService scoreService;
	private boolean error;
	private final ScoredRouteRepository scoredRouteRepository;
    private final TimestampService timestampService;
    private String location;

    @Inject
	public RouteSuggestionsPagePart(RouteService routeService, SessionContext sessionContext, ScoreService scoreService,
			ScoredRouteRepository scoredRouteRepository, TimestampService timestampService) {
		this.routeService = routeService;
		this.sessionContext = sessionContext;
		this.scoreService = scoreService;
		this.scoredRouteRepository = scoredRouteRepository;
        this.timestampService = timestampService;
    }

	public boolean isError() {
		return error;
	}

    public boolean isBeforeArrival() {
        LocalTime now = timestampService.getCurrentTime();
        return now.isBefore(sessionContext.getArrival());
    }

	public void setLocation(String location) {
		this.location = location;
	}

    public void setArrival(String arrival) {
        this.sessionContext.setArrival(StringUtils.isNotBlank(arrival) ? LocalTime.parse(arrival) : null);
    }

    public void setDeparture(String departure) {
        this.sessionContext.setDeparture(StringUtils.isNotBlank(departure) ? LocalTime.parse(departure) : null);
    }

	@Get
	public void get() {
		SustainifyUser user = sessionContext.getAuthentication().getUser();
		OrganisationLocation destination = findDestination(user);
		error = false;
		try {
			List<Route> routes = routeService.findRoutesFor(user, destination, sessionContext.getArrival(), sessionContext.getDeparture());
			List<ScoredRoute> scoredRoutes = Lists.newArrayList();
			for (Route route : routes) {
				scoredRoutes.add(scoreService.scoreFor(route, user));
			}
			sessionContext.setRoutes(scoredRoutes);
			sessionContext.setSelectedLocation(destination);
		} catch (Exception e) {
			LOGGER.error("Failed to find routes for " + user.getEmailAddress(), e);
			error = true;
		}
	}

	public String getDestination() {
		OrganisationLocation destination = findDestination(sessionContext.getAuthentication().getUser());
		return destination.getName();
	}

	private OrganisationLocation findDestination(SustainifyUser user) {
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			if (this.location.equals(location.getId())) {
				return location;
			}
		}
		return user.getOrganisation().getLocations().iterator().next();
	}

	public List<ScoredRoute> getRoutes() {
		return sessionContext.getRoutes();
	}

	public ScoredRoute getChosenRoute() {
		User user = sessionContext.getAuthentication().getUser();
		return scoredRouteRepository.findRouteByUserAndDay(user, timestampService.getCurrentDate());
	}
}
