package us.sustainify.common.domain.repository.organisation;

import us.sustainify.common.domain.model.organisation.Organisation;

public interface OrganisationRepository {
    void store(Organisation organisation);
}
