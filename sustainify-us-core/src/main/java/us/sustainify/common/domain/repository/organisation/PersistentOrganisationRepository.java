package us.sustainify.common.domain.repository.organisation;

import be.appify.framework.persistence.Persistence;
import be.appify.framework.repository.AbstractPersistentRepository;
import be.appify.framework.repository.TransactionalJob;
import us.sustainify.common.domain.model.organisation.Organisation;

public class PersistentOrganisationRepository extends AbstractPersistentRepository<Organisation> implements OrganisationRepository {
    public PersistentOrganisationRepository(Persistence persistence) {
        super(persistence, Organisation.class);
    }

    @Override
    public void store(final Organisation organisation) {
        doInTransaction(new TransactionalJob<Object, Organisation>() {
            @Override
            public Object execute() {
                store(organisation);
                return null;
            }
        });
    }
}
