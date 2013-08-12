package us.sustainify.commute.domain.service;

import java.util.List;

import org.joda.time.LocalTime;
import us.sustainify.common.domain.model.organisation.OrganisationLocation;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.route.Route;

public interface RouteService {

	List<Route> findRoutesFor(SustainifyUser user, OrganisationLocation destination);

    List<Route> findRoutesFor(SustainifyUser user, OrganisationLocation destination, LocalTime arrival, LocalTime departure);

}
