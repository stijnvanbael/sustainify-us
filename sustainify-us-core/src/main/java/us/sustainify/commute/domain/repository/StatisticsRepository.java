package us.sustainify.commute.domain.repository;

import org.joda.time.LocalDate;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.statistics.Aggregation;
import us.sustainify.commute.domain.model.statistics.Statistics;

public interface StatisticsRepository {
    Statistics getStatisticsFor(SustainifyUser user, LocalDate from, LocalDate to, Aggregation aggregateBy);
}
