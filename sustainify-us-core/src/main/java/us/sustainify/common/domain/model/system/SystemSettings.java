package us.sustainify.common.domain.model.system;

import be.appify.framework.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "system_settings")
public class SystemSettings extends AbstractEntity  {
    @Column(name = "google_api_key", length = 100)
    private String googleAPIKey = "";

    @Column(name = "wunderground_api_key", length = 100)
    private String wundergroundAPIKey = "";

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
}
