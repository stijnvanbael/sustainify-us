package us.sustainify.web.authenticated;


import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.location.service.LocationService;
import be.appify.framework.security.service.AuthenticationService;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import org.joda.time.LocalDate;
import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.commute.domain.model.statistics.Aggregation;
import us.sustainify.commute.domain.model.statistics.Statistics;
import us.sustainify.commute.domain.repository.StatisticsRepository;
import us.sustainify.web.SessionContext;

import javax.inject.Inject;

@At("/authenticated/graphs")
@Show("Graphs.html")
public class GraphsPage extends AbstractAuthenticatedPage {
    private StatisticsRepository statisticsRepository;
    private final SessionContext sessionContext;
    private LocalDate start;
    private LocalDate end;
    private Aggregation aggregation;

    @Inject
    public GraphsPage(StatisticsRepository statisticsRepository, SessionContext sessionContext,
                      AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService,
                      LocationService locationService) {
        super(sessionContext, authenticationService, locationService);
        this.statisticsRepository = statisticsRepository;
        this.sessionContext = sessionContext;
    }

    public GraphsViewModel getGraphs() {
        SustainifyUser user = sessionContext.getAuthentication().getUser();
        Statistics statistics = statisticsRepository.getStatisticsFor(user, start, end, aggregation);
        return new GraphsViewModel(statistics);
    }

    public void setStart(String start) {
        this.start = LocalDate.parse(start);
    }

    public void setEnd(String end) {
        this.end = LocalDate.parse(end);
    }

    public void setAggregation(String aggregation) {
        this.aggregation = Aggregation.valueOf(aggregation);
    }
}
