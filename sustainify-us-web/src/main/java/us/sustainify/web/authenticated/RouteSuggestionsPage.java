package us.sustainify.web.authenticated;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.joda.time.LocalTime;
import us.sustainify.common.domain.model.organisation.DayOfWeek;
import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.domain.User;
import be.appify.framework.security.service.AuthenticationService;

import com.google.common.collect.Lists;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;

@At("/authenticated/route-suggestions")
@Show("RouteSuggestions.html")
public class RouteSuggestionsPage extends AbstractAuthenticatedPage {
	private final ScoredRouteRepository scoredRouteRepository;
	private List<ScoredRoute> coworkerRoutes;

	@Inject
	public RouteSuggestionsPage(SessionContext sessionContext, AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
			LocationService locationService, ScoredRouteRepository scoredRouteRepository) {
		super(sessionContext, authenticationService, locationService);
		this.scoredRouteRepository = scoredRouteRepository;
	}

	@Get
	public void get() {
		SustainifyUser user = getSessionContext().getAuthentication().getUser();
		coworkerRoutes = scoredRouteRepository.findRecentRoutesByOrganisation(user.getOrganisation(), 20);
	}

    public boolean isBeforeArrival() {
        LocalTime now = LocalTime.now();
        return now.isBefore(getSessionContext().getArrival());
    }

	public List<ScoredRoute> getRoutes() {
		return getSessionContext().getRoutes();
	}

	public List<OrganisationLocationViewModel> getOrganisationLocations() {
		List<OrganisationLocationViewModel> organisationLocations = Lists.newArrayList();
		SustainifyUser user = getSessionContext().getAuthentication().getUser();
		for (OrganisationLocation loc : user.getOrganisation().getLocations()) {
			boolean selected = loc.equals(getSessionContext().getSelectedLocation());
			organisationLocations.add(new OrganisationLocationViewModel(loc, selected));
		}
		return organisationLocations;
	}

    public OfficeDayViewModel getOfficeDay()  {
        SustainifyUser user = getSessionContext().getAuthentication().getUser();
        if(getSessionContext().isTimesOverridden()) {
            return new OfficeDayViewModel(getSessionContext().getArrival(), getSessionContext().getDeparture());
        }
        return new OfficeDayViewModel(user.getPreferences().getOfficeHours().get(DayOfWeek.today().ordinal()));
    }

	public List<ScoredRoute> getCoworkerRoutes() {
		return coworkerRoutes;
	}

	public ScoredRoute getChosenRoute() {
		User user = getSessionContext().getAuthentication().getUser();
		return scoredRouteRepository.findRouteByUserAndDay(user, LocalDate.now());
	}

    public String getDestination() {
        return getSessionContext().getSelectedLocation().getName();
    }

}
