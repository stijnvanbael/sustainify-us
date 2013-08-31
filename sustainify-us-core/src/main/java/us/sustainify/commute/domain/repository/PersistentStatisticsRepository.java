package us.sustainify.commute.domain.repository;

import be.appify.framework.persistence.Persistence;
import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import be.appify.framework.repository.AbstractPersistentRepository;
import be.appify.framework.repository.TransactionalJob;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.joda.time.LocalDate;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.common.domain.repository.system.SystemSettingsRepository;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.statistics.Aggregation;
import us.sustainify.commute.domain.model.statistics.RouteStatistic;
import us.sustainify.commute.domain.model.statistics.Statistics;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PersistentStatisticsRepository extends AbstractPersistentRepository<RouteStatistic> implements StatisticsRepository {
    private static final String INDIVIDUAL_QUERY =
            "select sum(route.distance) as distance," +
                    " route.travel_mode," +
                    " year(scored_route.day) as year," +
                    " %1$s as interval," +
                    " '%2$s' as aggregation\n" +
            "from route\n" +
            "inner join scored_route on route.id = scored_route.route_id\n" +
            "where scored_route.user_id = :user_id\n" +
            "and scored_route.day between :start and :end\n" +
            "group by route.travel_mode, year(scored_route.day), %1$s\n" +
            "order by %1$s, route.travel_mode;";
    private static final Function<Object, RouteStatistic> MAPPER = new Function<Object, RouteStatistic>() {
        @Nullable
        @Override
        public RouteStatistic apply(@Nullable Object input) {
            Object[] array = (Object[]) input;
            assert array != null;
            Object period = array[3];
            Aggregation aggregation = Aggregation.valueOf((String) array[4]);
            RouteStatistic.Builder builder = RouteStatistic.create()
                    .distance(Length.meters(((BigDecimal) array[0]).doubleValue()))
                    .travelMode(TravelMode.valueOf((String) array[1]))
                    .year((Integer) array[2]);
            switch (aggregation) {
                case MONTH:
                    builder.month((Integer) period);
                    break;
                case WEEK:
                    builder.week((Integer) period);
                    break;
                case DAY:
                    builder.day(new LocalDate(period));
                    break;
            }
            return builder.build();
        }
    };
    private final SystemSettings systemSettings;

    public PersistentStatisticsRepository(Persistence persistence, SystemSettings systemSettings) {
        super(persistence, RouteStatistic.class);
        this.systemSettings = systemSettings;
    }

    @Override
    public Statistics getStatisticsFor(final SustainifyUser user, LocalDate from, LocalDate to, final Aggregation aggregateBy) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("user_id", user.getId());
        parameters.put("start", from.toDate());
        parameters.put("end", to.toDate());
        return doInTransaction(new TransactionalJob<Statistics, RouteStatistic>() {
            @Override
            public Statistics execute() {
                List<RouteStatistic> individualResults = find().byNativeQuery(String.format(INDIVIDUAL_QUERY, aggregation(aggregateBy), aggregateBy.name()), parameters, MAPPER).asList();
                return buildStatistics(individualResults);
            }
        });
    }

    private Statistics buildStatistics(List<RouteStatistic> individualResults) {
        Map<TravelMode, Mass> carbonEmissions = Maps.newHashMap();
        carbonEmissions.put(TravelMode.PUBLIC_TRANSIT, Mass.grams(systemSettings.getPublicTransitEmissions()));
        carbonEmissions.put(TravelMode.CAR, Mass.grams(systemSettings.getCarEmissions()));
        carbonEmissions.put(TravelMode.BICYCLING, Mass.grams(systemSettings.getBicyclingEmissions()));
        carbonEmissions.put(TravelMode.WALKING, Mass.grams(0.0));
        return Statistics.create()
                .individualStatistics(individualResults)
                .collectiveStatistics(individualResults)
                .numberOfEmployees(10)
                .carbonEmissions(carbonEmissions)
                .build();
    }

    private String aggregation(Aggregation aggregateBy) {
        switch (aggregateBy) {
            case MONTH:
                return "month(scored_route.day)";
            case WEEK:
                return "week(scored_route.day)";
        }
        return "scored_route.day";
    }
}
