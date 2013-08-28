package us.sustainify.commute.components;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.statistics.StatisticsCollection;

public class TestStatisticsCollection {
    private final StatisticsCollection collection;

    public TestStatisticsCollection(StatisticsCollection collection) {
        this.collection = collection;
    }

    public TestStatisticDataSet<Mass> carbonEmissions() {
        return new TestStatisticDataSet<>(collection.getCarbonEmissions());
    }

    public TestStatisticDataSet<Length> distanceBy(TravelMode travelMode) {
        return new TestStatisticDataSet<>(collection.getDistanceBy(travelMode));
    }
}
