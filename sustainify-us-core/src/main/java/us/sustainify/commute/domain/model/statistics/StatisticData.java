package us.sustainify.commute.domain.model.statistics;

import org.joda.time.Period;

public class StatisticData<T> {
    private T value;
    private Period period;

    public T getValue() {
        return value;
    }

    public Period getPeriod() {
        return period;
    }
}
