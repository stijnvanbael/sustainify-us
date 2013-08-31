package us.sustainify.commute.components;

import be.appify.framework.location.domain.Location;
import us.sustainify.common.domain.model.organisation.Organisation;
import us.sustainify.common.domain.model.organisation.OrganisationLocation;

public class TestOrganisation {
    private final Organisation organisation;
    private final TestSystem system;

    public TestOrganisation(String name, TestSystem system) {
        this.system = system;
        this.organisation = new Organisation(name,
                new OrganisationLocation("HQ", new Location("Rockvale", 34.6, 52.9)));
    }

    public TestUser user(String name) {
        TestUser user = new TestUser(name, this, system);
        system.store(user.getUser());
        return user;
    }

    public Organisation getOrganisation() {
        return organisation;
    }
}
