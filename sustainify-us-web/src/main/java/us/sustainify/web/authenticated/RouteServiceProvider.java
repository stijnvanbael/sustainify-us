package us.sustainify.web.authenticated;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.service.system.TimestampService;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.domain.User;
import be.appify.framework.security.service.AuthenticationService;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

@At("/authenticated/route")
@Service
public class RouteServiceProvider extends AbstractAuthenticatedPage {

	private int routeIndex;
	private final ScoredRouteRepository scoredRouteRepository;
    private final TimestampService timestampService;

    @Inject
	public RouteServiceProvider(SessionContext sessionContext, AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
			LocationService locationService, ScoredRouteRepository scoredRouteRepository, TimestampService timestampService) {
		super(sessionContext, authenticationService, locationService);
		this.scoredRouteRepository = scoredRouteRepository;
        this.timestampService = timestampService;
    }

	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	@Post
	public Reply<?> confirmRoute() {
		List<ScoredRoute> routes = getSessionContext().getRoutes();
		if (routes != null && routes.size() > routeIndex) {
			User user = getSessionContext().getAuthentication().getUser();
			ScoredRoute previousRoute = scoredRouteRepository.findRouteByUserAndDay(user, timestampService.getCurrentDate());
			if (previousRoute != null) {
				scoredRouteRepository.delete(previousRoute);
			}
			ScoredRoute chosenRoute = routes.get(routeIndex);
			scoredRouteRepository.store(chosenRoute);
		}
		return Reply.saying().redirect("/authenticated/route-suggestions?newRoute=" + routeIndex);
	}
}
