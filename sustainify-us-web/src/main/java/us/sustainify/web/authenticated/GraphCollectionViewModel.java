package us.sustainify.web.authenticated;

import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.statistics.StatisticsCollection;

public class GraphCollectionViewModel {
    private final StatisticsCollection collection;

    public GraphCollectionViewModel(StatisticsCollection collection) {
        this.collection = collection;
    }

    public GraphViewModel getCarbonEmissions() {
        return new GraphViewModel(collection.getCarbonEmissions());
    }

    public GraphViewModel getDistanceByCar() {
        return new GraphViewModel(collection.getDistanceBy(TravelMode.CAR));
    }

    public GraphViewModel getDistanceByPublicTransit() {
        return new GraphViewModel(collection.getDistanceBy(TravelMode.PUBLIC_TRANSIT));
    }

    public GraphViewModel getDistanceByBicycle() {
        return new GraphViewModel(collection.getDistanceBy(TravelMode.BICYCLING));
    }

}
