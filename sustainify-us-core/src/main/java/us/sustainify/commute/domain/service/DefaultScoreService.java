package us.sustainify.commute.domain.service;

import java.util.Set;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.score.RouteScore;

import com.google.common.collect.Sets;

public class DefaultScoreService implements ScoreService {

	private final Set<RouteScore> routeScores;

	public DefaultScoreService(RouteScore... routeScores) {
		this.routeScores = Sets.newHashSet(routeScores);
	}

	@Override
	public ScoredRoute scoreFor(Route route, SustainifyUser user) {
		int score = 0;
		for (RouteScore routeScore : routeScores) {
			score += routeScore.forRoute(route);
		}
		return new ScoredRoute(route, score, user);
	}

}
