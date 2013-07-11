package us.sustainify.common.domain.repository.system;

import us.sustainify.common.domain.model.system.SystemSettings;

public interface SystemSettingsRepository {
    SystemSettings getSystemSettings();
    void store(SystemSettings systemSettings);
}
