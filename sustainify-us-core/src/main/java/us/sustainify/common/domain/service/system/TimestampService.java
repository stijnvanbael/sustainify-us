package us.sustainify.common.domain.service.system;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public interface TimestampService {
    LocalDateTime getCurrentTimestamp();
    LocalDate getCurrentDate();
    LocalTime getCurrentTime();
}
