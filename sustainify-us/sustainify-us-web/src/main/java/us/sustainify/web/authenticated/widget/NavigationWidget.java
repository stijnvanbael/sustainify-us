package us.sustainify.web.authenticated.widget;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import us.sustainify.web.authenticated.UserViewModel;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.security.service.AuthenticationService;

import com.google.sitebricks.Show;
import com.google.sitebricks.rendering.EmbedAs;

@EmbedAs("Navigation")
@Show("Navigation.html")
public class NavigationWidget {
	private final AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService;
	private final SessionContext sessionContext;

	@Inject
	public NavigationWidget(AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService, SessionContext sessionContext) {
		this.authenticationService = authenticationService;
		this.sessionContext = sessionContext;
	}

	public String getSignOutURL() {
		return authenticationService.getSignOutURL();
	}

	public UserViewModel getUser() {
		return new UserViewModel(sessionContext.getAuthentication().getUser());
	}
}
