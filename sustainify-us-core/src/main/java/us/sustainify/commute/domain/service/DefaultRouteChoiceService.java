package us.sustainify.commute.domain.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import us.sustainify.commute.domain.model.score.UserScore;
import us.sustainify.commute.domain.repository.ScoredRouteRepository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DefaultRouteChoiceService implements RouteChoiceService {
	private final ScoredRouteRepository scoredRouteRepository;

	@Inject
	public DefaultRouteChoiceService(ScoredRouteRepository scoredRouteRepository) {
		this.scoredRouteRepository = scoredRouteRepository;
	}

	@Override
	public void chooseRoute(ScoredRoute scoredRoute) {
		ScoredRoute existingRoute = scoredRouteRepository.findRouteByUserAndDay(scoredRoute.getUser(), scoredRoute.getDay());
		if (existingRoute != null) {
			scoredRouteRepository.delete(existingRoute);
		}
		scoredRouteRepository.store(scoredRoute);
	}

	@Override
	public List<ScoredRoute> findCoworkerRoutes(SustainifyUser user) {
		return scoredRouteRepository.findRecentRoutesByOrganisation(user.getOrganisation(), 20);
	}

	@Override
	public List<UserScore> getLeaderboard(Organisation organisation) {
		List<ScoredRoute> routes = scoredRouteRepository.findRoutesByOrganisationOfLastDays(organisation, 7);
		Map<SustainifyUser, UserScore> scores = Maps.newHashMap();
		for (ScoredRoute route : routes) {
			SustainifyUser user = route.getUser();
			UserScore userScore = scores.get(user);
			if (userScore == null) {
				userScore = new UserScore(user);
				scores.put(user, userScore);
			}
			userScore.addScoredRoute(route);
		}
		List<UserScore> scoreList = Lists.newArrayList(scores.values());
		Collections.sort(scoreList);
		return scoreList;
	}
}
