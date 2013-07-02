package us.sustainify.web.authenticated;

import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import be.appify.framework.security.repository.UserRepository;

import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

@At("/authenticated/default-location")
@Service
public class DefaultLocationServiceProvider {
	private final UserRepository<SustainifyUser> userRepository;
	private final SessionContext sessionContext;
	private String workLocation;

	@Inject
	private DefaultLocationServiceProvider(UserRepository<SustainifyUser> userRepository, SessionContext sessionContext) {
		this.userRepository = userRepository;
		this.sessionContext = sessionContext;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	@Post
	public Reply<?> post() {
		SustainifyUser user = sessionContext.getAuthentication().getUser();
		for (OrganisationLocation location : user.getOrganisation().getLocations()) {
			if (location.getId().equals(workLocation)) {
				user.getPreferences().setDefaultLocation(location);
				break;
			}
		}
		userRepository.store(user);
		return Reply.saying().ok();
	}
}
