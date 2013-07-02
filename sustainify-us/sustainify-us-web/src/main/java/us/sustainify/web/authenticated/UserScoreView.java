package us.sustainify.web.authenticated;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.score.UserScore;

public class UserScoreView {
	private final UserScore userScore;
	private final int rank;

	public UserScoreView(int rank, UserScore userScore) {
		this.userScore = userScore;
		this.rank = rank;
	}

	public String getUser() {
		SustainifyUser user = userScore.getUser();
		return user.getFirstName() + " " + user.getLastName();
	}

	public int getScore() {
		return userScore.getScore();
	}

	public int getRank() {
		return rank;
	}

}
