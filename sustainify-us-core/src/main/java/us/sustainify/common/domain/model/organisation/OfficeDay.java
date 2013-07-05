package us.sustainify.common.domain.model.organisation;

import org.joda.time.LocalTime;

public class OfficeDay {
	private LocalTime arrival;
	private LocalTime departure;

	public LocalTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalTime arrival) {
		this.arrival = arrival;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

}
