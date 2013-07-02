package us.sustainify.web.authenticated;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.service.AuthenticationService;

public abstract class AbstractAuthenticatedPage {
	private final SessionContext sessionContext;
	private final AuthenticationService<SustainifyUser, ?> authenticationService;
	private final PreferencesViewModel preferencesViewModel;

	public AbstractAuthenticatedPage(SessionContext sessionContext,
			AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
			LocationService locationService) {
		this.sessionContext = sessionContext;
		this.authenticationService = authenticationService;
		this.preferencesViewModel = new PreferencesViewModel(sessionContext.getAuthentication().getUser(), locationService);
	}

	public UserViewModel getUser() {
		return new UserViewModel(sessionContext.getAuthentication().getUser());
	}

	public PreferencesViewModel getPreferences() {
		return preferencesViewModel;
	}

	public String getSignOutURL() {
		return authenticationService.getSignOutURL();
	}

	protected SessionContext getSessionContext() {
		return sessionContext;
	}

	protected AuthenticationService<SustainifyUser, ?> getAuthenticationService() {
		return authenticationService;
	}
}
