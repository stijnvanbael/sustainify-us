package us.sustainify.commute.components;

import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.commute.domain.model.route.TravelMode;

public class TestSystemSettings {
    private final SystemSettings systemSettings;

    public TestSystemSettings(SystemSettings systemSettings) {
        this.systemSettings = systemSettings;
    }

    public TestSystemSettings averageCarbonEmissions(TravelMode travelMode, int grammesPerKilometer) {
        systemSettings.setCarbonEmissions(travelMode, grammesPerKilometer);
        return this;
    }
}
