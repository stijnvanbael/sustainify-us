package us.sustainify.commute.components;

import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

public class TestUserHistory {
    protected final TestUser user;
    protected final TestSystem system;
    protected int offset = 0;

    public TestUserHistory(TestUser user, TestSystem system, int offset) {
        this.user = user;
        this.system = system;
        this.offset = offset;
    }

    public TestUserHistoryRoute route(TravelMode travelMode) {
        return new TestUserHistoryRoute(user, system, travelMode, null, offset);
    }

    public TestUserHistoryRoute route(VehicleType vehicleType) {
        return new TestUserHistoryRoute(user, system, TravelMode.PUBLIC_TRANSIT, vehicleType, offset);
    }


}
