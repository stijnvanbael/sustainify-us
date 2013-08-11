package us.sustainify.common.domain.model.organisation;

import java.util.List;

import javax.persistence.*;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @OrderColumn(name = "day_of_week", nullable = false)
	private final List<OfficeDay> officeDays;

	public UserPreferences() {
		officeDays = Lists.newArrayList();
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
		return officeDays;
	}
}
