package us.sustainify.common.domain.model.organisation;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import be.appify.framework.security.domain.User;

@Entity
@DiscriminatorValue("SUSTAINIFY")
public class SustainifyUser extends User {
	private static final long serialVersionUID = 2289026921816866336L;
	@ManyToOne(cascade = CascadeType.MERGE, optional = false)
	@JoinColumn(name = "organisation_id")
	private Organisation organisation;
	@Embedded
	private UserPreferences preferences;

	protected SustainifyUser() {
		this.preferences = new UserPreferences();
	}

	public UserPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(UserPreferences preferences) {
		this.preferences = preferences;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public static Builder createNew() {
		return new Builder();
	}

	public static class Builder extends User.Builder<SustainifyUser, Builder> {

		public Builder() {
			super(SustainifyUser.class);
		}

		public Builder organisation(@NotNull Organisation organisation) {
			return set("organisation", organisation);
		}

	}
}
