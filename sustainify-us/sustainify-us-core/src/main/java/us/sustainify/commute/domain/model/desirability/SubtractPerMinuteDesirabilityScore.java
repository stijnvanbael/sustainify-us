package us.sustainify.commute.domain.model.desirability;

import us.sustainify.commute.domain.model.route.Route;

public class SubtractPerMinuteDesirabilityScore extends DesirabilityScore {

	private final DesirabilityScore desirabilityScore;
	private final int score;

	public SubtractPerMinuteDesirabilityScore(DesirabilityScore desirabilityScore, int score) {
		super(desirabilityScore.getTravelMode());
		this.desirabilityScore = desirabilityScore;
		this.score = score;
	}

	@Override
	public int forRouteInternal(Route route) {
		return desirabilityScore.forRoute(route) - (int) route.getDuration().getStandardMinutes() * score;
	}

}
