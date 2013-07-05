package us.sustainify.web.authenticated;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.domain.Authentication;
import be.appify.framework.security.service.AuthenticationService;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

@At("/authenticated/sign-out")
@Service
public class SignOutServiceProvider extends AbstractAuthenticatedPage {

	@Inject
	public SignOutServiceProvider(SessionContext sessionContext, AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
			LocationService locationService) {
		super(sessionContext, authenticationService, locationService);
	}

	@Get
	public Reply<?> signOut() {
		Authentication<SustainifyUser> authentication = getSessionContext().getAuthentication();
		getAuthenticationService().cancel(authentication);
		getSessionContext().destroy();
		return Reply.saying().redirect("/");
	}

}
