package us.sustainify.commute.domain.repository;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import be.appify.framework.persistence.Persistence;
import be.appify.framework.repository.AbstractPersistentRepository;
import be.appify.framework.repository.TransactionalJob;
import be.appify.framework.security.domain.User;

public class PersistentScoredRouteRepository extends AbstractPersistentRepository<ScoredRoute> implements ScoredRouteRepository {

	@Inject
	public PersistentScoredRouteRepository(Persistence persistence) {
		super(persistence, ScoredRoute.class);
	}

	@Override
	public void store(final ScoredRoute scoredRoute) {
		doInTransaction(new TransactionalJob<Void, ScoredRoute>() {
			@Override
			public Void execute() {
				this.store(scoredRoute);
				return null;
			}
		});
	}

	@Override
	public List<ScoredRoute> findRecentRoutesByOrganisation(final Organisation organisation, final int limit) {
		return doInTransaction(new TransactionalJob<List<ScoredRoute>, ScoredRoute>() {
			@Override
			public List<ScoredRoute> execute() {
				return find().where("user.organisation").equalTo(organisation).orderBy("day").descending().limit(limit).asList();
			}
		});
	}

	@Override
	public List<ScoredRoute> findRoutesByOrganisationOfLastDays(final Organisation organisation, final int days) {
		return doInTransaction(new TransactionalJob<List<ScoredRoute>, ScoredRoute>() {
			@Override
			public List<ScoredRoute> execute() {
				LocalDate now = LocalDate.now();
				LocalDate from = now.minusDays(days);
				return find().where("user.organisation").equalTo(organisation).and("day").greaterThanOrEqualTo(from.toDate()).orderBy("day").descending()
						.asList();
			}
		});
	}

	@Override
	public ScoredRoute findRouteByUserAndDay(final User user, final LocalDate day) {
		return doInTransaction(new TransactionalJob<ScoredRoute, ScoredRoute>() {
			@Override
			public ScoredRoute execute() {
				return find().where("user").equalTo(user).and("day").equalTo(day).asSingle();
			}
		});
	}

	@Override
	public void delete(final ScoredRoute scoredRoute) {
		doInTransaction(new TransactionalJob<Void, ScoredRoute>() {

			@Override
			public Void execute() {
				this.delete(scoredRoute);
				return null;
			}
		});
	}

}
