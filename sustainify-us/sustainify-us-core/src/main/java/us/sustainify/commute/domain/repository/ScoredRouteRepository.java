package us.sustainify.commute.domain.repository;

import java.util.List;

import org.joda.time.LocalDate;

import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import be.appify.framework.security.domain.User;

public interface ScoredRouteRepository {

	void store(ScoredRoute scoredRoute);

	List<ScoredRoute> findRecentRoutesByOrganisation(Organisation organisation, int limit);

	List<ScoredRoute> findRoutesByOrganisationOfLastDays(Organisation organisation, int days);

	ScoredRoute findRouteByUserAndDay(User user, LocalDate day);

	void delete(ScoredRoute scoredRoute);

}
