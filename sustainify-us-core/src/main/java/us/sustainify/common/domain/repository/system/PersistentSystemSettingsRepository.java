package us.sustainify.common.domain.repository.system;

import be.appify.framework.persistence.Persistence;
import be.appify.framework.repository.AbstractPersistentRepository;
import be.appify.framework.repository.TransactionalJob;
import us.sustainify.common.domain.model.system.SystemSettings;

public class PersistentSystemSettingsRepository extends AbstractPersistentRepository<SystemSettings> implements SystemSettingsRepository {
    public PersistentSystemSettingsRepository(Persistence persistence) {
        super(persistence, SystemSettings.class);
    }

    @Override
    public SystemSettings getSystemSettings() {
        SystemSettings systemSettings = doInTransaction(new TransactionalJob<SystemSettings, SystemSettings>() {
            @Override
            public SystemSettings execute() {
                return find().asSingle();
            }
        });
        if(systemSettings == null) {
            systemSettings = new SystemSettings();
        }
        return systemSettings;
    }

    @Override
    public void store(final SystemSettings systemSettings) {
        doInTransaction(new TransactionalJob<Void, SystemSettings>() {
            @Override
            public Void execute() {
                SystemSettings settingsToSave = find().asSingle();
                if(settingsToSave == null) {
                    settingsToSave = new SystemSettings();
                }
                systemSettings.copyInto(settingsToSave);
                store(settingsToSave);
                return null;
            }
        });
    }

}
