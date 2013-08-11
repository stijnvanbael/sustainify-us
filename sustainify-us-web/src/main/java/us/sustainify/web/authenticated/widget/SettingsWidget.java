package us.sustainify.web.authenticated.widget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import us.sustainify.web.authenticated.PreferencesViewModel;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.service.AuthenticationService;

import com.google.sitebricks.Show;
import com.google.sitebricks.rendering.EmbedAs;

@EmbedAs("Settings")
@Show("Settings.html")
public class SettingsWidget {
	private String target;
	private final SessionContext sessionContext;
	private final AuthenticationService<SustainifyUser, ?> authenticationService;
	private final LocationService locationService;
    private PreferencesViewModel preferences;

    @Inject
	public SettingsWidget(SessionContext sessionContext, LocationService locationService,
			AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService) {
		this.sessionContext = sessionContext;
		this.locationService = locationService;
		this.authenticationService = authenticationService;
        this.preferences = new PreferencesViewModel(sessionContext.getAuthentication().getUser(), locationService);
	}

	@Inject
	public void setRequest(HttpServletRequest request) {
		target = request.getRequestURI() + "?" + request.getQueryString();
	}

	public String getTarget() {
		return target;
	}

	public PreferencesViewModel getPreferences() {
		return preferences;
	}

	public String getSignOutURL() {
		return authenticationService.getSignOutURL();
	}
}
