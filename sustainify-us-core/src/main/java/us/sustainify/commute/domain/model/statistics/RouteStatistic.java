package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.domain.ReflectionBuilder;
import be.appify.framework.quantity.Length;
import org.joda.time.*;
import us.sustainify.commute.domain.model.route.TravelMode;

import javax.persistence.Entity;

@Entity
public class RouteStatistic {
    private Length distance;
    private TravelMode travelMode;
    private int year;
    private Integer month;
    private Integer week;
    private LocalDate day;

    public static Builder create() {
        return new Builder();
    }

    public Length getDistance() {
        return distance;
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public Interval getInterval() {
        if(month != null) {
            LocalDate start = new LocalDate().withYear(year).withMonthOfYear(month).withDayOfMonth(1);
            return new Interval(start.toDateTime(LocalTime.MIDNIGHT), Duration.standardDays(start.dayOfMonth().getMaximumValue()));
        } else if(week != null) {
            LocalDate start = new LocalDate().withYear(year).withWeekOfWeekyear(week).withDayOfWeek(1);
            return new Interval(start.toDateTime(LocalTime.MIDNIGHT), Duration.standardDays(7));
        }
        return day.toInterval();
    }

    public static class Builder extends ReflectionBuilder<RouteStatistic, Builder> {

        protected Builder() {
            super(RouteStatistic.class);
        }

        public Builder distance(Length distance) {
            return set("distance", distance);
        }

        public Builder travelMode(TravelMode travelMode) {
            return set("travelMode", travelMode);
        }

        public Builder year(int year) {
            return set("year", year);
        }

        public Builder month(Integer month) {
            return set("month", month);
        }

        public Builder week(Integer week) {
            return set("week", week);
        }

        public Builder day(LocalDate day) {
            return set("day", day);
        }
    }
}
