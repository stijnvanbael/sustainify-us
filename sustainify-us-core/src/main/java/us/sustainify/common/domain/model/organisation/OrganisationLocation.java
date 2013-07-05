package us.sustainify.common.domain.model.organisation;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import be.appify.framework.domain.AbstractEntity;
import be.appify.framework.location.domain.Location;

@Table(name = "organisation_location")
@Entity
public class OrganisationLocation extends AbstractEntity {

	private static final long serialVersionUID = 1415635628237590566L;

	@Column(length = 100, nullable = false)
	private String name;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "address")),
	})
	private Location location;

	@ManyToOne(optional = false)
	@JoinColumn(name = "organisation_id")
	private Organisation organisation;

	@Column(name = "sort_order", nullable = false)
	private int order;

	@SuppressWarnings("unused")
	private OrganisationLocation() {
	}

	public OrganisationLocation(String name, Location location) {
		this.name = name;
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
