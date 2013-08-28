package us.sustainify.commute.components;

import us.sustainify.commute.domain.model.statistics.Statistics;

public class TestStatistics {
    private final Statistics statistics;

    public TestStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public TestStatisticsCollection collective() {
        return new TestStatisticsCollection(statistics.getCollective());
    }

    public TestStatisticsCollection individual() {
        return new TestStatisticsCollection(statistics.getIndividual());
    }

    public TestStatisticsCollection average() {
        return new TestStatisticsCollection(statistics.getAverage());
    }
}
