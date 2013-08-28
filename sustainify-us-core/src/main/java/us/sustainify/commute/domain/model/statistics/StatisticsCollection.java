package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import us.sustainify.commute.domain.model.route.TravelMode;

public class StatisticsCollection {
    public StatisticDataSet<Mass> getCarbonEmissions() {
        return null;
    }

    public StatisticDataSet<Length> getDistanceBy(TravelMode travelMode) {
        return null;
    }
}
