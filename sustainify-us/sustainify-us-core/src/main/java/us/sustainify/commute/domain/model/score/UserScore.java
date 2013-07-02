package us.sustainify.commute.domain.model.score;

import java.util.List;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;

import com.google.common.collect.Lists;

public class UserScore implements Comparable<UserScore> {
	private final SustainifyUser user;
	private final List<ScoredRoute> scoredRoutes;

	public UserScore(SustainifyUser user) {
		this.user = user;
		this.scoredRoutes = Lists.newArrayList();
	}

	public void addScoredRoute(ScoredRoute scoredRoute) {
		if (!scoredRoute.getUser().equals(user)) {
			throw new IllegalArgumentException("Scored route is for user " + scoredRoute.getUser() + " but expected for user " + user);
		}
		scoredRoutes.add(scoredRoute);
	}

	public SustainifyUser getUser() {
		return user;
	}

	public int getScore() {
		int score = 0;
		for (ScoredRoute route : scoredRoutes) {
			score += route.getScore();
		}
		return score;
	}

	@Override
	public int compareTo(UserScore other) {
		return Integer.valueOf(other.getScore()).compareTo(getScore());
	}

}
