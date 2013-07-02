package us.sustainify.web.authenticated;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.repository.UserRepository;
import be.appify.framework.security.service.AuthenticationService;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

// TODO: rename to preferences
@At("/authenticated/settings")
@Service
public class SettingsServiceProvider extends AbstractAuthenticatedPage {
	private String target;
	private final UserRepository<SustainifyUser> userRepository;

	@Inject
	public SettingsServiceProvider(SessionContext sessionContext,
			AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
			UserRepository<SustainifyUser> userRepository,
			LocationService locationService) {
		super(sessionContext, authenticationService, locationService);
		this.userRepository = userRepository;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Post
	public Reply<?> saveSettings() {
		SustainifyUser user = getSessionContext().getAuthentication().getUser();
		getSessionContext().setRoutes(null);
		userRepository.store(user);
		return Reply.saying().redirect(target);
	}
}
