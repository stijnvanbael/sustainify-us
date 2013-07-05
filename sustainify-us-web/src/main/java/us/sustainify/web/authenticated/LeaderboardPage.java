package us.sustainify.web.authenticated;

import java.util.List;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.score.UserScore;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;
import us.sustainify.commute.domain.service.RouteChoiceService;
import us.sustainify.web.SessionContext;
import be.appify.framework.location.service.LocationService;

import com.google.common.collect.Lists;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;

@At("/authenticated/leaderboard")
@Show("Leaderboard.html")
public class LeaderboardPage {
	private final RouteChoiceService routeChoiceService;
	private final ScoredRouteRepository scoredRouteRepository;
	private final SessionContext sessionContext;
	private final LocationService locationService;

	@Inject
	public LeaderboardPage(RouteChoiceService routeChoiceService, SessionContext sessionContext, ScoredRouteRepository scoredRouteRepository,
			LocationService locationService) {
		this.routeChoiceService = routeChoiceService;
		this.sessionContext = sessionContext;
		this.scoredRouteRepository = scoredRouteRepository;
		this.locationService = locationService;
	}

	public PreferencesViewModel getPreferences() {
		return new PreferencesViewModel(sessionContext.getAuthentication().getUser(), locationService);
	}

	public List<UserScoreView> getLeaderboard() {
		SustainifyUser user = sessionContext.getAuthentication().getUser();
		List<UserScore> scores = routeChoiceService.getLeaderboard(user.getOrganisation());
		List<UserScoreView> scoreViews = Lists.newArrayList();
		int rank = 1;
		for (UserScore userScore : scores) {
			scoreViews.add(new UserScoreView(rank, userScore));
			rank++;
		}
		return scoreViews;
	}

	public List<ScoredRoute> getCoworkerRoutes() {
		SustainifyUser user = sessionContext.getAuthentication().getUser();
		return scoredRouteRepository.findRecentRoutesByOrganisation(user.getOrganisation(), 20);
	}

}
