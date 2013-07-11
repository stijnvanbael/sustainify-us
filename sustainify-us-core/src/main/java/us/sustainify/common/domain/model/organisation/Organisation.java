package us.sustainify.common.domain.model.organisation;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.appify.framework.domain.AbstractEntity;
import be.appify.framework.domain.ReflectionBuilder;
import be.appify.framework.location.domain.Location;

import com.google.common.collect.Lists;

@Entity
public class Organisation extends AbstractEntity {
	private static final long serialVersionUID = -7775459140968055386L;

	@Column(length = 50, nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation", orphanRemoval = true)
	@OrderColumn(name = "sort_order", nullable = false)
	private List<OrganisationLocation> locations;

	@SuppressWarnings("unused")
	private Organisation() {
	}

    public static Builder createNew() {
        return new Builder();
    }

	public Organisation(String name, OrganisationLocation... locations) {
		this.name = name;
		setLocations(Lists.newArrayList(locations));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OrganisationLocation> getLocations() {
		return Lists.newArrayList(locations);
	}

	public void setLocations(List<OrganisationLocation> locations) {
		this.locations = Lists.newArrayList();
		for (OrganisationLocation location : locations) {
			addLocation(location);
		}
	}

	public void addLocation(OrganisationLocation location) {
		locations.add(location);
		location.setOrganisation(this);
		location.setOrder(locations.indexOf(location));
	}

	public void removeLocation(OrganisationLocation location) {
		locations.remove(location);
	}

	public static class Builder extends ReflectionBuilder<Organisation, Builder> {

		public Builder() {
			super(Organisation.class);
		}

		public Builder name(@NotNull @Size(min = 1, max = 50) String name) {
			return set("name", name);
		}

		public Builder locations(@NotNull List<OrganisationLocation> locations) {
			return set("locations", locations);
		}

        @Override
        public Organisation build() {
            Organisation organisation = super.build();
            for(OrganisationLocation location : organisation.locations) {
                location.setOrganisation(organisation);
                location.setOrder(organisation.locations.indexOf(location));
            }
            return organisation;
        }
    }

}
