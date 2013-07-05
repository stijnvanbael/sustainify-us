package us.sustainify.commute.domain.repository;

import us.sustainify.commute.domain.model.route.Route;

public interface RouteRepository {
	Route findByCode(String code);
}
