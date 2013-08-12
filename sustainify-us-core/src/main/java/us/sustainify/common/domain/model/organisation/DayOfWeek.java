package us.sustainify.common.domain.model.organisation;

import org.joda.time.DateMidnight;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfWeek today() {
        return values()[new DateMidnight().getDayOfWeek() - 1];
    }
}
