package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import com.google.common.collect.Maps;
import org.joda.time.Interval;
import us.sustainify.commute.domain.model.route.TravelMode;

import java.util.List;
import java.util.Map;

public class StatisticsCollection {
    private StatisticDataSet<Mass> carbonEmissions;
    private Map<TravelMode, StatisticDataSet<Length>> distance;

    public StatisticsCollection(List<RouteStatistic> statistics, Map<TravelMode, Mass> carbonEmissions, long divisor) {
        this.carbonEmissions = new StatisticDataSet<>(Calculator.MASS);
        this.distance = Maps.newHashMap();
        for (TravelMode travelMode : TravelMode.values()) {
            StatisticDataSet<Length> distance = new StatisticDataSet<>(Calculator.LENGTH);
            this.distance.put(travelMode, distance);
            Mass emissionsPer100km = carbonEmissions.get(travelMode);
            for (RouteStatistic routeStatistic : statistics) {
                Interval interval = routeStatistic.getInterval();
                if(routeStatistic.getTravelMode() == travelMode) {
                    this.carbonEmissions.add(interval, emissionsPer100km.multiply(routeStatistic.getDistance().getKilometers() / (50 * divisor)));
                    distance.add(interval, routeStatistic.getDistance().multiply(2.0 / divisor));
                }
            }
        }

    }

    public StatisticsCollection(List<RouteStatistic> statistics, Map<TravelMode, Mass> carbonEmissions) {
        this(statistics, carbonEmissions, 1);
    }

    public StatisticDataSet<Mass> getCarbonEmissions() {
        return carbonEmissions;
    }

    public StatisticDataSet<Length> getDistanceBy(TravelMode travelMode) {
        return distance.get(travelMode);
    }
}
