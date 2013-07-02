package us.sustainify.commute.domain.service.google.directions;

import com.google.api.client.util.Key;

public class TransitDetails {
	@Key
	private TransitStop arrival_stop;

	@Key
	private TransitStop departure_stop;

	@Key
	private TransitTime arrival_time;

	@Key
	private TransitTime departure_time;

	@Key
	private String headsign;

	@Key
	private TransitLine line;

	@Key
	private Integer num_stops;

	public TransitStop getArrival_stop() {
		return arrival_stop;
	}

	public void setArrival_stop(TransitStop arrival_stop) {
		this.arrival_stop = arrival_stop;
	}

	public TransitStop getDeparture_stop() {
		return departure_stop;
	}

	public void setDeparture_stop(TransitStop departure_stop) {
		this.departure_stop = departure_stop;
	}

	public TransitTime getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(TransitTime arrival_time) {
		this.arrival_time = arrival_time;
	}

	public TransitTime getDeparture_time() {
		return departure_time;
	}

	public void setDeparture_time(TransitTime departure_time) {
		this.departure_time = departure_time;
	}

	public String getHeadsign() {
		return headsign;
	}

	public void setHeadsign(String headsign) {
		this.headsign = headsign;
	}

	public TransitLine getLine() {
		return line;
	}

	public void setLine(TransitLine line) {
		this.line = line;
	}

	public Integer getNum_stops() {
		return num_stops;
	}

	public void setNum_stops(Integer num_stops) {
		this.num_stops = num_stops;
	}

}
