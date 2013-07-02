package us.sustainify.common.domain.model.organisation;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import be.appify.framework.location.domain.Location;

import com.google.common.collect.Lists;

@Embeddable
public class UserPreferences {
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "home_location_name")),
			@AttributeOverride(name = "latitude", column = @Column(name = "home_location_latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "home_location_longitude"))
	})
	private Location homeLocation;

	@ManyToOne
	@JoinColumn(name = "default_location_id")
	private OrganisationLocation defaultLocation;

	@Transient
	private final List<OfficeDay> officeDays;

	public UserPreferences() {
		officeDays = Lists.newArrayList();
		for (int i = 0; i < 7; i++) {
			officeDays.add(new OfficeDay());
		}
	}

	public Location getHomeLocation() {
		return homeLocation;
	}

	public void setHomeLocation(Location homeLocation) {
		this.homeLocation = homeLocation;
	}

	public void setDefaultLocation(OrganisationLocation selectedLocation) {
		this.defaultLocation = selectedLocation;
	}

	public OrganisationLocation getDefaultLocation() {
		return defaultLocation;
	}

	public List<OfficeDay> getOfficeHours() {
		return Lists.newArrayList(officeDays);
	}
}
