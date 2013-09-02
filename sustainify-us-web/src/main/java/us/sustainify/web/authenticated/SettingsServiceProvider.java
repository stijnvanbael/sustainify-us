package us.sustainify.web.authenticated;

import javax.inject.Inject;

import us.sustainify.common.domain.model.organisation.DayOfWeek;
import us.sustainify.common.domain.model.organisation.OfficeDay;
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

import java.util.List;

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
        OfficeDay officeDay = getOfficeDay(user);
        getSessionContext().setArrival(officeDay.getArrival());
        getSessionContext().setDeparture(officeDay.getDeparture());
		userRepository.store(user);
		return Reply.saying().redirect(target);
	}

    private OfficeDay getOfficeDay(SustainifyUser user) {
        List<OfficeDay> officeHours = user.getPreferences().getOfficeHours();
        int dayOfWeek = DayOfWeek.today().ordinal();
        if(officeHours.size() > dayOfWeek) {
            return officeHours.get(dayOfWeek);
        }
        OfficeDay officeDay = new OfficeDay(user, DayOfWeek.today(), null, null);
        return  officeDay;
    }
}
