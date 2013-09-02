package us.sustainify.commute.components;

import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.commute.domain.model.route.TravelMode;

public class TestSystemSettings {
    private final SystemSettings systemSettings;

    public TestSystemSettings(SystemSettings systemSettings) {
        this.systemSettings = systemSettings;
    }

    public TestSystemSettings averageCarbonEmissions(TravelMode travelMode, int grammesPerKilometer) {
        switch (travelMode) {
            case BICYCLING:
                systemSettings.setBicycleEmissions(grammesPerKilometer);
                break;
            case PUBLIC_TRANSIT:
                systemSettings.setPublicTransitEmissions(grammesPerKilometer);
                break;
            case CAR:
                systemSettings.setCarEmissions(grammesPerKilometer);
                break;
        }
        return this;
    }
}
