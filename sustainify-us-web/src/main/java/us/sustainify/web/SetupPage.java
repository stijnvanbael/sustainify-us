package us.sustainify.web;

import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.domain.Location;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.location.service.google.GoogleLocationService;
import be.appify.framework.security.domain.Credential;
import be.appify.framework.security.service.EncryptionService;
import be.appify.framework.weather.service.WeatherService;
import be.appify.framework.weather.service.wunderground.WundergroundWeatherService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Post;
import org.apache.commons.lang.Validate;
import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.common.domain.service.system.SystemSetupService;

import javax.inject.Inject;

@At("/setup")
@Show("Setup.html")
public class SetupPage {
    private final SystemSetupService systemSetupService;
    private final GoogleLocationService locationService;
    private final WundergroundWeatherService weatherService;
    private final EncryptionService encryptionService;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    private String organisationName;
    private String organisationHQLocation;
    private String googleAPIKey;
    private String wundergroundAPIKey;

    @Inject
    public SetupPage(SystemSetupService systemSetupService, LocationService locationService,
                     WeatherService weatherService, EncryptionService encryptionService) {
        this.systemSetupService = systemSetupService;
        this.locationService = (GoogleLocationService) locationService;
        this.weatherService = (WundergroundWeatherService) weatherService;
        this.encryptionService = encryptionService;
    }

    @Post
    public Reply<?> completeSetup() {
        Validate.notNull(firstName);
        Validate.notNull(lastName);
        Validate.notNull(emailAddress);
        Validate.notNull(password);
        Validate.notNull(organisationName);
        Validate.notNull(organisationHQLocation);
        Validate.notNull(googleAPIKey);
        Validate.notNull(wundergroundAPIKey);

        locationService.setAPIKey(googleAPIKey);
        weatherService.setAPIKey(wundergroundAPIKey);
        SystemSettings systemSettings = new SystemSettings();
        systemSettings.setGoogleAPIKey(googleAPIKey);
        systemSettings.setWundergroundAPIKey(wundergroundAPIKey);

        Location location = locationService.getLocation(organisationHQLocation);

        OrganisationLocation hqLocation = new OrganisationLocation("HQ", location);
        Organisation organisation = Organisation.createNew()
                .name(organisationName)
                .locations(Lists.newArrayList(hqLocation))
                .build();
        String encryptedPassword = encryptionService.encrypt(password);
        Credential<SustainifyUser> credential = new SimpleCredential<>(emailAddress, encryptedPassword);
        SustainifyUser user = SustainifyUser.createNew()
                .firstName(firstName)
                .lastName(lastName)
                .emailAddress(emailAddress)
                .organisation(organisation)
                .credentials(Sets.<Credential<?>>newHashSet(credential))
                .build();

        systemSetupService.storeAdministrator(user);
        systemSetupService.storeSystemSettings(systemSettings);
        return Reply.saying().redirect("/");
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public void setOrganisationHQLocation(String organisationHQLocation) {
        this.organisationHQLocation = organisationHQLocation;
    }

    public void setGoogleAPIKey(String googleAPIKey) {
        this.googleAPIKey = googleAPIKey;
    }

    public void setWundergroundAPIKey(String wundergroundAPIKey) {
        this.wundergroundAPIKey = wundergroundAPIKey;
    }
}
