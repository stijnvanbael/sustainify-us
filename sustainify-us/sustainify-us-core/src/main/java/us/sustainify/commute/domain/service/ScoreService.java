package us.sustainify.commute.domain.service;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.Route;
import us.sustainify.commute.domain.model.route.ScoredRoute;

public interface ScoreService {

	ScoredRoute scoreFor(Route route, SustainifyUser user);

}
