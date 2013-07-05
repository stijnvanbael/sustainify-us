package us.sustainify.commute.domain.service.google.directions;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.api.client.util.Key;

@XmlType
public class Polyline {
	@Key
	private String points;

	@XmlElement
	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

}
