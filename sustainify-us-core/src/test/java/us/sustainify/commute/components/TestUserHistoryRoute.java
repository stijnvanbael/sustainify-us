package us.sustainify.commute.components;

import org.joda.time.LocalDate;
import us.sustainify.common.domain.model.organisation.OfficeDay;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

import java.util.List;

public class TestUserHistoryRoute extends TestUserHistory {
    private final TravelMode travelMode;
    private final VehicleType vehicleType;
    private int offset = 0;

    public TestUserHistoryRoute(TestUser user, TestSystem system, TravelMode travelMode, VehicleType vehicleType) {
        super(user, system);
        this.travelMode = travelMode;
        this.vehicleType = vehicleType;
    }

    public TestUserHistoryRoute times(int times) {
        List<OfficeDay> officeHours = user.getUser().getPreferences().getOfficeHours();
        for(int i = 0; i < times; i++) {
            OfficeDay officeDay = officeHours.get(offset % 7);
            while(officeDay.getArrival() == null) {
                offset++;
                officeDay = officeHours.get(offset % 7);
            }
            system.confirmRoute(user.getUser(), travelMode, vehicleType, TestSystem.START.plusDays(offset));
            offset++;
        }
        return this;
    }
}
