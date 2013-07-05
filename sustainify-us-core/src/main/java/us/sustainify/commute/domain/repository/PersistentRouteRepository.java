package us.sustainify.commute.domain.repository;

import java.util.List;

import us.sustainify.commute.domain.model.route.Route;
import be.appify.framework.persistence.Persistence;
import be.appify.framework.repository.AbstractPersistentRepository;
import be.appify.framework.repository.TransactionalJob;

public class PersistentRouteRepository extends AbstractPersistentRepository<Route> implements RouteRepository {

	public PersistentRouteRepository(Persistence persistence) {
		super(persistence, Route.class);
	}

	@Override
	public Route findByCode(final String code) {
		return doInTransaction(new TransactionalJob<Route, Route>() {
			@Override
			public Route execute() {
				// FIXME: assure uniqueness of route codes
				List<Route> routes = find().where("code").equalTo(code).asList();
				return routes.isEmpty() ? null : routes.get(0);
			}
		});
	}

}
