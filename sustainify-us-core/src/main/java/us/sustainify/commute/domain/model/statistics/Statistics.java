package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.quantity.Mass;
import us.sustainify.commute.domain.model.route.TravelMode;

import java.util.List;
import java.util.Map;

public class Statistics {
    private StatisticsCollection individual;
    private StatisticsCollection average;
    private StatisticsCollection collective;

    public Statistics(StatisticsCollection individual, StatisticsCollection average, StatisticsCollection collective) {
        this.individual = individual;
        this.average = average;
        this.collective = collective;
    }

    public StatisticsCollection getCollective() {
        return collective;
    }

    public StatisticsCollection getIndividual() {
        return individual;
    }

    public StatisticsCollection getAverage() {
        return average;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private List<RouteStatistic> individualStatistics;
        private List<RouteStatistic> collectiveStatistics;
        private Map<TravelMode, Mass> carbonEmissions;
        private long numberOfEmployees;

        public Builder individualStatistics(List<RouteStatistic> individualStatistics) {
            this.individualStatistics = individualStatistics;
            return this;
        }

        public Builder collectiveStatistics(List<RouteStatistic> collectiveStatistics) {
            this.collectiveStatistics = collectiveStatistics;
            return this;
        }

        public Builder carbonEmissions(Map<TravelMode, Mass> carbonEmissions) {
            this.carbonEmissions = carbonEmissions;
            return this;
        }

        public Builder numberOfEmployees(long numberOfEmployees) {
            this.numberOfEmployees = numberOfEmployees;
            return this;
        }

        public Statistics build() {
            StatisticsCollection individual = new StatisticsCollection(individualStatistics, carbonEmissions);
            StatisticsCollection collective = new StatisticsCollection(collectiveStatistics, carbonEmissions);
            StatisticsCollection average = new StatisticsCollection(collectiveStatistics, carbonEmissions, numberOfEmployees);
            return new Statistics(individual, average, collective);
        }
    }
}
