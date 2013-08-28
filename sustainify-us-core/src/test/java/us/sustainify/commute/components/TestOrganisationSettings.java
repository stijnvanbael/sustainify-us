package us.sustainify.commute.components;

import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

/**
 *
 */
public class TestOrganisationSettings {
    private final TestOrganisation organisation;

    public TestOrganisationSettings(TestOrganisation organisation) {
        this.organisation = organisation;
    }

    public TestOrganisationSettings averageCarbonEmissions(TravelMode travelMode, int grammesPerKilometer) {
        return this;  //To change body of created methods use File | Settings | File Templates.
    }

    public TestOrganisationSettings averageCarbonEmissions(VehicleType vehicleType, int grammesPerKilometer) {
        return this;  //To change body of created methods use File | Settings | File Templates.
    }
}
