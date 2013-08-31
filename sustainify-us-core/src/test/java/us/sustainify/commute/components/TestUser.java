package us.sustainify.commute.components;

import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.domain.Location;
import be.appify.framework.quantity.Length;
import be.appify.framework.security.domain.Credential;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.joda.time.LocalTime;
import us.sustainify.common.domain.model.organisation.*;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

import java.util.List;

public class TestUser {

    private final SustainifyUser user;
    private static final LocalTime TIME_08_00 = LocalTime.parse("08:00");
    private static final LocalTime TIME_17_00 = LocalTime.parse("17:00");
    private final TestSystem system;

    public TestUser(String name, TestOrganisation organisation, TestSystem system) {
        this.system = system;
        Organisation o = organisation.getOrganisation();
        String emailAddress = toURLString(name) + "@" + toURLString(o.getName()) + ".com";
        this.user = SustainifyUser.createNew()
                .organisation(o)
                .emailAddress(emailAddress)
                .firstName(name)
                .lastName("Doe")
                .credentials(Sets.<Credential<?>>newHashSet(new SimpleCredential<SustainifyUser>(emailAddress, "W6ph5Mm5Pz8GgiULbPgzG37mj9g=")))
                .build();
        UserPreferences preferences = user.getPreferences();
        preferences.getOfficeHours().addAll(Lists.newArrayList(
                new OfficeDay(user, DayOfWeek.MONDAY, TIME_08_00, TIME_17_00),
                new OfficeDay(user, DayOfWeek.TUESDAY, TIME_08_00, TIME_17_00),
                new OfficeDay(user, DayOfWeek.WEDNESDAY, TIME_08_00, TIME_17_00),
                new OfficeDay(user, DayOfWeek.THURSDAY, TIME_08_00, TIME_17_00),
                new OfficeDay(user, DayOfWeek.FRIDAY, TIME_08_00, TIME_17_00),
                new OfficeDay(user, DayOfWeek.SATURDAY, null, null),
                new OfficeDay(user, DayOfWeek.SUNDAY, null, null)));
        preferences.setHomeLocation(new Location(name + "'s home", Double.parseDouble("34." + name.hashCode()), Double.parseDouble("56." + name.hashCode())));
        preferences.setDefaultLocation(organisation.getOrganisation().getLocations().get(0));
    }

    private String toURLString(String name) {
        return name.toLowerCase().replaceAll("\\s", "");
    }

    public TestUser route(TravelMode travelMode, Length distance) {
        system.defineRoute(user, travelMode, null, distance);
        return this;
    }

    public TestUser route(VehicleType vehicleType, Length distance) {
        system.defineRoute(user, TravelMode.PUBLIC_TRANSIT, vehicleType, distance);
        return this;
    }

    public TestUserHistory history() {
        return new TestUserHistory(this, system, 0);
    }

    public SustainifyUser getUser() {
        return user;
    }

    public TestStatistics getStatistics() {
        return system.getStatisticsFor(this);
    }
}
