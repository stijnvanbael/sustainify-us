package us.sustainify.commute.domain.service.google.directions;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.api.client.util.Key;

@XmlType
public class GoogleRoute {
	@Key
	private Set<RouteLeg> legs;

	@Key
	private Polyline overview_polyline;

	@XmlElement
	public Set<RouteLeg> getLegs() {
		return legs;
	}

	public void setLegs(Set<RouteLeg> legs) {
		this.legs = legs;
	}

	@XmlElement
	public Polyline getOverview_polyline() {
		return overview_polyline;
	}

	public void setOverview_polyline(Polyline overview_polyline) {
		this.overview_polyline = overview_polyline;
	}
}
