package us.sustainify.web.authenticated;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalTime;
import us.sustainify.common.domain.model.organisation.DayOfWeek;
import us.sustainify.common.domain.model.organisation.OfficeDay;
import us.sustainify.common.domain.model.organisation.SustainifyUser;

public class OfficeDayViewModel {
    private final OfficeDay officeDay;
    private static final String FORMAT = "HH:mm";

    public OfficeDayViewModel(OfficeDay officeDay) {
        this.officeDay = officeDay;
    }

    public OfficeDayViewModel(SustainifyUser user, DayOfWeek dayOfWeek, LocalTime arrival, LocalTime departure) {
        this.officeDay = new OfficeDay(user, dayOfWeek, arrival, departure);
    }

    public String getArrival() {
		return officeDay.getArrival() != null ? officeDay.getArrival().toString(FORMAT) : "";
	}

	public void setArrival(String arrival) {
        officeDay.setArrival(StringUtils.isNotBlank(arrival) ? LocalTime.parse(arrival) : null);
	}

	public String getDeparture() {
        return officeDay.getDeparture() != null ? officeDay.getDeparture().toString(FORMAT) : "";
	}

	public void setDeparture(String departure) {
        officeDay.setDeparture(StringUtils.isNotBlank(departure) ? LocalTime.parse(departure) : null);
	}

}
