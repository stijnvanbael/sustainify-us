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

    @Column(name = "car_emissions", nullable = false)
    private double carEmissions;

    @Column(name = "public_transit_emissions", nullable = false)
    private double publicTransitEmissions;

    @Column(name = "bicycle_emissions", nullable = false)
    private double bicycleEmissions;

    public SystemSettings() {
    }

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

    public double getCarEmissions() {
        return carEmissions;
    }

    public double getPublicTransitEmissions() {
        return publicTransitEmissions;
    }

    public double getBicycleEmissions() {
        return bicycleEmissions;
    }

    public void setCarEmissions(double carEmissions) {
        this.carEmissions = carEmissions;
    }

    public void setPublicTransitEmissions(double publicTransitEmissions) {
        this.publicTransitEmissions = publicTransitEmissions;
    }

    public void setBicycleEmissions(double bicycleEmissions) {
        this.bicycleEmissions = bicycleEmissions;
    }

    public void copyInto(SystemSettings other) {
        other.setGoogleAPIKey(googleAPIKey);
        other.setWundergroundAPIKey(wundergroundAPIKey);
        other.setBicycleEmissions(bicycleEmissions);
        other.setCarEmissions(carEmissions);
        other.setPublicTransitEmissions(publicTransitEmissions);
    }
}
