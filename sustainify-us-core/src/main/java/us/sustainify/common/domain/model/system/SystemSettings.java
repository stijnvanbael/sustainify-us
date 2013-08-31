package us.sustainify.common.domain.model.system;

import be.appify.framework.domain.AbstractEntity;
import us.sustainify.commute.domain.model.route.TravelMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "system_settings")
public class SystemSettings extends AbstractEntity {
    @Column(name = "google_api_key", length = 100)
    private String googleAPIKey = "";

    @Column(name = "wunderground_api_key", length = 100)
    private String wundergroundAPIKey = "";

    @Column(name = "car_emissions", nullable = false, columnDefinition = "numeric default 0")
    private int carEmissions;

    @Column(name = "public_transit_emissions", nullable = false, columnDefinition = "numeric default 0")
    private int publicTransitEmissions;

    @Column(name = "bicycling_emissions", nullable = false, columnDefinition = "numeric default 0")
    private int bicyclingEmissions;

    public String getWundergroundAPIKey() {
        return wundergroundAPIKey;
    }

    public void setWundergroundAPIKey(String wundergroundAPIKey) {
        this.wundergroundAPIKey = wundergroundAPIKey;
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }

    public void setGoogleAPIKey(String googleAPIKey) {
        this.googleAPIKey = googleAPIKey;
    }

    public void setCarbonEmissions(TravelMode travelMode, int grammesPerKilometer) {
        switch (travelMode) {
            case CAR:
                carEmissions = grammesPerKilometer;
                break;
            case PUBLIC_TRANSIT:
                publicTransitEmissions = grammesPerKilometer;
                break;
            case BICYCLING:
                bicyclingEmissions = grammesPerKilometer;
                break;
        }
    }

    public Integer getCarEmissions() {
        return carEmissions;
    }

    public Integer getPublicTransitEmissions() {
        return publicTransitEmissions;
    }

    public Integer getBicyclingEmissions() {
        return bicyclingEmissions;
    }
}
