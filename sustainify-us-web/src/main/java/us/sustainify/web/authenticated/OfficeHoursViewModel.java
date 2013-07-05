package us.sustainify.web.authenticated;

public class OfficeHoursViewModel {
	private final OfficeDayViewModel monday;
	private final OfficeDayViewModel tuesday;
	private final OfficeDayViewModel wednesday;
	private final OfficeDayViewModel thursday;
	private final OfficeDayViewModel friday;
	private final OfficeDayViewModel saturday;
	private final OfficeDayViewModel sunday;

	public OfficeHoursViewModel() {
		monday = new OfficeDayViewModel();
		tuesday = new OfficeDayViewModel();
		wednesday = new OfficeDayViewModel();
		thursday = new OfficeDayViewModel();
		friday = new OfficeDayViewModel();
		saturday = new OfficeDayViewModel();
		sunday = new OfficeDayViewModel();
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
