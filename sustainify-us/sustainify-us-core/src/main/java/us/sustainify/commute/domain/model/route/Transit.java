package us.sustainify.commute.domain.model.route;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

@Embeddable
public class Transit {
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTimeAsTimestamp")
	@Column(name = "departure_time", nullable = true)
	private LocalTime departureTime;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTimeAsTimestamp")
	@Column(name = "arrival_time", nullable = true)
	private LocalTime arrivalTime;

	@Column(nullable = true, length = 100)
	private String headsign;

	@Enumerated(EnumType.STRING)
	@Column(name = "vehicle_type", nullable = true)
	private VehicleType vehicleType;

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public String getHeadsign() {
		return headsign;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public void setHeadsign(String headsign) {
		this.headsign = headsign;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

}
