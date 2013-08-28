package us.sustainify.commute.domain.repository;

import be.appify.framework.persistence.Persistence;
import be.appify.framework.repository.AbstractPersistentRepository;
import org.joda.time.LocalDate;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.statistics.Aggregation;
import us.sustainify.commute.domain.model.statistics.Statistics;

public class PersistentStatisticsRepository extends AbstractPersistentRepository<Statistics> implements StatisticsRepository {
    public PersistentStatisticsRepository(Persistence persistence) {
        super(persistence, Statistics.class);
    }

    @Override
    public Statistics getStatisticsFor(SustainifyUser user, LocalDate from, LocalDate to, Aggregation aggregateBy) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
