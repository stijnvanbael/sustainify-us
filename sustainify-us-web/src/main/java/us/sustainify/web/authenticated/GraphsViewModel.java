package us.sustainify.web.authenticated;

import us.sustainify.commute.domain.model.statistics.Statistics;

public class GraphsViewModel {
    private final Statistics statistics;

    public GraphsViewModel(Statistics statistics) {
        this.statistics = statistics;
    }

    public GraphCollectionViewModel getCollective() {
        return new GraphCollectionViewModel(statistics.getCollective());
    }

    public GraphCollectionViewModel getIndividual() {
        return new GraphCollectionViewModel(statistics.getIndividual());
    }

    public GraphCollectionViewModel getAverage() {
        return new GraphCollectionViewModel(statistics.getAverage());
    }
}
