package us.sustainify.commute.domain.model.statistics;

public class Statistics {
    private StatisticsCollection individual;
    private StatisticsCollection average;
    private StatisticsCollection collective;

    public StatisticsCollection getCollective() {
        return collective;
    }

    public StatisticsCollection getIndividual() {
        return individual;
    }

    public StatisticsCollection getAverage() {
        return average;
    }
}
