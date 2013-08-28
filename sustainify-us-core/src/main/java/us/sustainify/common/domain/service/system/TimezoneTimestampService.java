package us.sustainify.common.domain.service.system;

import org.joda.time.*;

public class TimezoneTimestampService implements TimestampService {
    private DateTimeZone timeZone;

    public TimezoneTimestampService(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now(timeZone);
    }

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now(timeZone);
    }

    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now(timeZone);
    }
}
