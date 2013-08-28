package us.sustainify.commute.components;

import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

public class TestUserHistory {
    protected final TestUser user;
    protected final TestSystem system;

    public TestUserHistory(TestUser user, TestSystem system) {
        this.user = user;
        this.system = system;
    }

    public TestUserHistoryRoute route(TravelMode travelMode) {
        return new TestUserHistoryRoute(user, system, travelMode, null);
    }

    public TestUserHistoryRoute route(VehicleType vehicleType) {
        return new TestUserHistoryRoute(user, system, TravelMode.PUBLIC_TRANSIT, vehicleType);
    }


}
