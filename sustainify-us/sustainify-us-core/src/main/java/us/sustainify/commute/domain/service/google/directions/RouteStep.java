package us.sustainify.commute.domain.service.google.directions;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.api.client.util.Key;

@XmlType
public class RouteStep {

	@Key
	private Numeric distance;
	@Key
	private Numeric duration;
	@Key
	private List<RouteStep> steps;
	@Key
	private String travel_mode;
	@Key
	private Coordinates start_location;
	@Key
	private Coordinates end_location;
	@Key
	private Polyline polyline;
	@Key
	private TransitDetails transit_details;

	@XmlElement
	public Numeric getDistance() {
		return distance;
	}

	public void setDistance(Numeric distance) {
		this.distance = distance;
	}

	@XmlElement
	public Numeric getDuration() {
		return duration;
	}

	public void setDuration(Numeric duration) {
		this.duration = duration;
	}

	@XmlElement
	public List<RouteStep> getSteps() {
		return steps;
	}

	public void setSteps(List<RouteStep> steps) {
		this.steps = steps;
	}

	@XmlElement
	public String getTravel_mode() {
		return travel_mode;
	}

	public void setTravel_mode(String travel_mode) {
		this.travel_mode = travel_mode;
	}

	@XmlElement
	public Coordinates getStart_location() {
		return start_location;
	}

	public void setStart_location(Coordinates start_location) {
		this.start_location = start_location;
	}

	@XmlElement
	public Coordinates getEnd_location() {
		return end_location;
	}

	public void setEnd_location(Coordinates end_location) {
		this.end_location = end_location;
	}

	@XmlElement
	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public TransitDetails getTransit_details() {
		return transit_details;
	}

	public void setTransit_details(TransitDetails transit_details) {
		this.transit_details = transit_details;
	}
}
