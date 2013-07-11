package us.sustainify.common.domain.service.system;

import be.appify.framework.security.repository.UserRepository;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.common.domain.model.system.SystemSettings;
import us.sustainify.common.domain.repository.system.SystemSettingsRepository;

import javax.inject.Inject;

public class SystemSetupService {
    private UserRepository<SustainifyUser> userRepository;
    private SystemSettingsRepository systemSettingsRepository;

    @Inject
    public SystemSetupService(SystemSettingsRepository systemSettingsRepository, UserRepository<SustainifyUser> userRepository) {
        this.systemSettingsRepository = systemSettingsRepository;
        this.userRepository = userRepository;
    }

    public boolean isSetupRequired() {
        return userRepository.count() == 0;
    }

    public void storeAdministrator(SustainifyUser user) {
        userRepository.store(user);
    }

    public void storeSystemSettings(SystemSettings systemSettings) {
        systemSettingsRepository.store(systemSettings);
    }

}
