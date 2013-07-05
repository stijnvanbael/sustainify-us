package us.sustainify.commute.domain.model.desirability;

import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.TravelMode;

public class FixedDesirabilityScore extends DesirabilityScore {

	private final int score;

	public FixedDesirabilityScore(TravelMode travelMode, int score) {
		super(travelMode);
		this.score = score;
	}

	@Override
	protected int forRouteInternal(Route route) {
		return score;
	}
}
