package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.quantity.Mass;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

public class StatisticDataSet<T> {
    private List<StatisticData<T>> data;
    private Calculator<T> calculator;

    public StatisticDataSet(Calculator<T> calculator) {
        this.data = Lists.newArrayList();
        this.calculator = calculator;
    }

    public int size() {
        return data.size();
    }

    public List<StatisticData<T>> getData() {
        return data;
    }

    public StatisticData<T> get(int index) {
        return data.get(index);
    }

    public T get(LocalDate start, LocalDate end) {
        T total = calculator.zero();
        for(StatisticData<T> d : this.data) {
            DateTime startDateTime = start.toDateTime(LocalTime.MIDNIGHT);
            DateTime endDateTime = end.toDateTime(LocalTime.MIDNIGHT).plusDays(1);
            if((d.getInterval().getStart().equals(startDateTime) || d.getInterval().getStart().isAfter(startDateTime))
                    && d.getInterval().getEnd().equals(endDateTime) || d.getInterval().getEnd().isBefore(endDateTime)) {
                 total = calculator.add(total, d.getValue());
            }
        }
        return total;
    }

    public void add(Interval interval, T value) {
        for(StatisticData<T> d : this.data) {
            if(d.getInterval().equals(interval)) {
                d.add(value);
                return;
            }
        }
        data.add(new StatisticData<>(calculator, value, interval));
        Collections.sort(data);
    }
}