package us.sustainify.web.authenticated;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.model.organisation.UserPreferences;
import be.appify.framework.location.domain.Location;
import be.appify.framework.location.service.LocationService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PreferencesViewModel {

	private final SustainifyUser user;
	private final LocationService locationService;
	private List<String> organisationLocationNames;
	private List<String> organisationLocationIds;
	private List<String> organisationLocationAddresses;
	private final List<OrganisationLocationViewModel> organisationLocations;
	private final OfficeHoursViewModel officeHours;

	public PreferencesViewModel(SustainifyUser user, LocationService locationService) {
		this.user = user;
		this.locationService = locationService;
		this.organisationLocations = Lists.newArrayList();
		this.officeHours = new OfficeHoursViewModel();
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			boolean selected = location.equals(user.getPreferences().getDefaultLocation());
			organisationLocations.add(new OrganisationLocationViewModel(location, selected));
		}
	}

	public OfficeHoursViewModel getOfficeHours() {
		return officeHours;
	}

	public String getHomeLocationName() {
		UserPreferences preferences = user.getPreferences();
		Location homeLocation = preferences != null ? preferences.getHomeLocation() : null;
		return homeLocation != null ? homeLocation.getName() : "";
	}

	public List<OrganisationLocationViewModel> getOrganisationLocations() {
		return organisationLocations;
	}

	public List<String> getOrganisationLocationNames() {
		List<String> names = Lists.newArrayList();
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			names.add(location.getName());
		}
		return names;
	}

	public List<String> getOrganisationLocationIds() {
		List<String> ids = Lists.newArrayList();
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			ids.add(location.getId());
		}
		return ids;
	}

	public List<String> getOrganisationLocationAddresses() {
		List<String> addresses = Lists.newArrayList();
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			addresses.add(location.getLocation().getName());
		}
		return addresses;
	}

	public String getOrganisationName() {
		return user.getOrganisation().getName();
	}

	public void setHomeLocationName(String homeLocationName) {
		Location location = locationService.getLocation(homeLocationName);
		UserPreferences preferences = user.getPreferences();
		if (preferences == null) {
			preferences = new UserPreferences();
			user.setPreferences(preferences);
		}
		preferences.setHomeLocation(location);
	}

	public void setWorkLocation(String workLocation) {
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			if (location.getId().equals(workLocation)) {
				user.getPreferences().setDefaultLocation(location);
				break;
			}
		}
	}

	public void setOrganisationName(String organisationName) {
		user.getOrganisation().setName(organisationName);
	}

	public void setOrganisationLocationNames(List<String> organisationLocationNames) {
		this.organisationLocationNames = organisationLocationNames;
		updateOrganisationLocations();
	}

	public void setOrganisationLocationAddresses(List<String> organisationLocationAddresses) {
		this.organisationLocationAddresses = organisationLocationAddresses;
		updateOrganisationLocations();
	}

	public void setOrganisationLocationIds(List<String> organisationLocationIds) {
		this.organisationLocationIds = organisationLocationIds;
		updateOrganisationLocations();
	}

	private void updateOrganisationLocations() {
		OrganisationLocation location = null;
		if (organisationLocationNames != null && organisationLocationAddresses != null && organisationLocationIds != null) {
			for (int i = 0; i < organisationLocationIds.size(); i++) {
				String name = organisationLocationNames.get(i);
				String address = organisationLocationAddresses.get(i);
				if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(address)) {
					String id = organisationLocationIds.get(i);
					location = findExistingLocation(id);
					if (location != null) {
						updateLocation(location, name, address, i);
					} else {
						location = createLocation(name, address, i);
						organisationLocationIds.set(i, location.getId());
					}
				}
			}
			Collection<OrganisationLocation> toRemove = Sets.newHashSet();
			for (OrganisationLocation loc : user.getOrganisation().getLocations()) {
				if (!organisationLocationIds.contains(loc.getId())) {
					toRemove.add(loc);
				}
			}
			for (OrganisationLocation loc : toRemove) {
				user.getOrganisation().removeLocation(loc);
			}
		}
	}

	private OrganisationLocation createLocation(String name, String address, int order) {
		Location location = locationService.getLocation(address);
		OrganisationLocation newLocation = new OrganisationLocation(name, location);
		newLocation.setOrder(order);
		user.getOrganisation().addLocation(newLocation);
		return newLocation;
	}

	private void updateLocation(OrganisationLocation existingLocation, String name, String address, int order) {
		existingLocation.setName(name);
		existingLocation.setOrder(order);
		if (!existingLocation.getLocation().getName().equals(address)) {
			Location location = locationService.getLocation(address);
			existingLocation.setLocation(location);
		}
	}

	private OrganisationLocation findExistingLocation(String id) {
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			if (id.equals(location.getId())) {
				return location;
			}
		}
		return null;
	}
}
