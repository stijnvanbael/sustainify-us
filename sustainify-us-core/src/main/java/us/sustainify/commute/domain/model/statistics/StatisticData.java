package us.sustainify.commute.domain.model.statistics;

import org.joda.time.Interval;

public class StatisticData<T> implements Comparable<StatisticData<T>> {
    private final Calculator<T> calculator;
    private T value;
    private Interval interval;

    public StatisticData(Calculator<T> calculator, T value, Interval interval) {
        this.calculator = calculator;
        this.value = value;
        this.interval = interval;
    }

    public T getValue() {
        return value;
    }

    public Interval getInterval() {
        return interval;
    }

    public void add(T value) {
        this.value = calculator.add(this.value, value);
    }

    @Override
    public int compareTo(StatisticData<T> o) {
        return interval.getStart().compareTo(o.getInterval().getStart());
    }
}
