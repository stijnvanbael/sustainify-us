package us.sustainify.web;

import java.io.Serializable;
import java.util.List;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.ScoredRoute;
import be.appify.framework.security.domain.Authentication;

import com.google.inject.servlet.SessionScoped;

@SessionScoped
public class SessionContext implements Serializable {
	private static final long serialVersionUID = -6163054529711035248L;
	private Authentication<SustainifyUser> authentication;
	private List<ScoredRoute> routes;
	private OrganisationLocation selectedLocation;

	public void setAuthentication(Authentication<SustainifyUser> authentication) {
		this.authentication = authentication;
		this.selectedLocation = authentication.getUser().getPreferences().getDefaultLocation();
	}

	public Authentication<SustainifyUser> getAuthentication() {
		return authentication;
	}

	public void setRoutes(List<ScoredRoute> routes) {
		this.routes = routes;
	}

	public List<ScoredRoute> getRoutes() {
		return routes;
	}

	public void destroy() {
		authentication = null;
		routes = null;
	}

	public void setSelectedLocation(OrganisationLocation selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public OrganisationLocation getSelectedLocation() {
		return selectedLocation;
	}
}
