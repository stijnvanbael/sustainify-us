package us.sustainify.web.authenticated;

import us.sustainify.common.domain.model.organisation.DayOfWeek;
import us.sustainify.common.domain.model.organisation.OfficeDay;
import us.sustainify.common.domain.model.organisation.SustainifyUser;

import java.util.List;

public class OfficeHoursViewModel {
	private final OfficeDayViewModel monday;
	private final OfficeDayViewModel tuesday;
	private final OfficeDayViewModel wednesday;
	private final OfficeDayViewModel thursday;
	private final OfficeDayViewModel friday;
	private final OfficeDayViewModel saturday;
	private final OfficeDayViewModel sunday;

	public OfficeHoursViewModel(SustainifyUser user) {
        List<OfficeDay> officeHours = user.getPreferences().getOfficeHours();
		monday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.MONDAY));
		tuesday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.TUESDAY));
		wednesday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.WEDNESDAY));
		thursday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.THURSDAY));
		friday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.FRIDAY));
		saturday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.SATURDAY));
		sunday = new OfficeDayViewModel(getOfficeDay(user, officeHours, DayOfWeek.SUNDAY));
	}

    private static OfficeDay getOfficeDay(SustainifyUser user, List<OfficeDay> officeHours, DayOfWeek dayOfWeek) {
        if(dayOfWeek.ordinal() < officeHours.size()) {
            return officeHours.get(dayOfWeek.ordinal());
        }
        OfficeDay day = new OfficeDay();
        day.setUser(user);
        day.setDayOfWeek(dayOfWeek);
        officeHours.add(day);
        return day;
    }

    public OfficeDayViewModel getMonday() {
		return monday;
	}

	public OfficeDayViewModel getTuesday() {
		return tuesday;
	}

	public OfficeDayViewModel getWednesday() {
		return wednesday;
	}

	public OfficeDayViewModel getThursday() {
		return thursday;
	}

	public OfficeDayViewModel getFriday() {
		return friday;
	}

	public OfficeDayViewModel getSaturday() {
		return saturday;
	}

	public OfficeDayViewModel getSunday() {
		return sunday;
	}

}
