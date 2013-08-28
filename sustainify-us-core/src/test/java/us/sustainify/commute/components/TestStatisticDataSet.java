package us.sustainify.commute.components;

import org.joda.time.LocalDate;
import us.sustainify.commute.domain.model.statistics.StatisticDataSet;

public class TestStatisticDataSet<T> {
    private final StatisticDataSet<T> dataSet;

    public TestStatisticDataSet(StatisticDataSet<T> dataSet) {
        this.dataSet = dataSet;
    }

    public T between(LocalDate start, LocalDate end) {
        return dataSet.get(start, end);
    }
}
