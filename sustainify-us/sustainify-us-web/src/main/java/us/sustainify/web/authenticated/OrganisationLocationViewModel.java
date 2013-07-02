package us.sustainify.web.authenticated;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;

public class OrganisationLocationViewModel {
	private final OrganisationLocation organisationLocation;
	private final boolean selected;

	public OrganisationLocationViewModel(OrganisationLocation organisationLocation, boolean selected) {
		this.organisationLocation = organisationLocation;
		this.selected = selected;
	}

	public int getIndex() {
		return organisationLocation.getOrder();
	}

	public String getName() {
		return organisationLocation.getName();
	}

	public String getAddress() {
		return organisationLocation.getLocation().getName();
	}

	public String getId() {
		return organisationLocation.getId();
	}

	public boolean isSelected() {
		return selected;
	}
}
