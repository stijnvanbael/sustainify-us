package us.sustainify.commute.domain.service;

import java.util.List;

import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.score.UserScore;

public interface RouteChoiceService {

	void chooseRoute(ScoredRoute scoredRoute);

	List<ScoredRoute> findCoworkerRoutes(SustainifyUser user);

	List<UserScore> getLeaderboard(Organisation organisation);

}
