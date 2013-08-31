package us.sustainify.commute;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import us.sustainify.commute.components.*;
import us.sustainify.commute.domain.model.route.TravelMode;
import us.sustainify.commute.domain.model.route.VehicleType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StatisticsAcceptanceTests {

    private TestOrganisation organisation;
    private TestSystem system;
    private TestUser sarah;

    @Before
    public void before() {
        this.system = new TestSystem();
        this.organisation = system.organisation("Test Lab");
    }

    private void given() {
        sarah = organisation.user("Sarah")
                .route(TravelMode.CAR, Length.kilometers(12))
                .route(TravelMode.PUBLIC_TRANSIT, Length.kilometers(12))
                .route(TravelMode.BICYCLING, Length.kilometers(13));
        TestUser mark = organisation.user("Mark")
                .route(TravelMode.CAR, Length.kilometers(16))
                .route(TravelMode.PUBLIC_TRANSIT, Length.kilometers(16))
                .route(TravelMode.BICYCLING, Length.kilometers(18));
        TestUser tina = organisation.user("Tina")
                .route(TravelMode.CAR, Length.kilometers(6))
                .route(TravelMode.PUBLIC_TRANSIT, Length.kilometers(6))
                .route(TravelMode.BICYCLING, Length.kilometers(6));

        system.settings().averageCarbonEmissions(TravelMode.CAR, 120)
                .averageCarbonEmissions(TravelMode.PUBLIC_TRANSIT, 90);

        sarah.history().route(TravelMode.CAR).times(9)
                .route(TravelMode.PUBLIC_TRANSIT).times(6)
                .route(TravelMode.BICYCLING).times(6);
        mark.history().route(TravelMode.CAR).times(21);
        tina.history().route(TravelMode.PUBLIC_TRANSIT).times(5)
                .route(TravelMode.BICYCLING).times(15);
    }

    @Test
    public void collectiveCarbonEmissions() {
        given();

        TestStatistics statistics = sarah.getStatistics();
        TestStatisticDataSet<Mass> collectiveCarbonEmissions = statistics.collective().carbonEmissions();
        assertThat(collectiveCarbonEmissions.between(TestSystem.START, TestSystem.END), equalTo(Mass.grams(1249.2)));
    }

    @Test
    public void collectiveDistancePerTravelMode() {
        given();

        TestStatistics statistics = sarah.getStatistics();
        TestStatisticDataSet<Length> collectiveDistanceByCar = statistics.collective().distanceBy(TravelMode.CAR);
        assertThat(collectiveDistanceByCar.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(888)));

        TestStatisticDataSet<Length> collectiveDistanceByPublicTransit = statistics.collective().distanceBy(TravelMode.PUBLIC_TRANSIT);
        assertThat(collectiveDistanceByPublicTransit.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(204)));

        TestStatisticDataSet<Length> collectiveDistanceByBicycle = statistics.collective().distanceBy(TravelMode.BICYCLING);
        assertThat(collectiveDistanceByBicycle.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(336)));
    }

    @Test
    public void individualCarbonEmissions() {
        given();

        TestStatistics statistics = sarah.getStatistics();
        TestStatisticDataSet<Mass> individualCarbonEmissions = statistics.individual().carbonEmissions();
        assertThat(individualCarbonEmissions.between(TestSystem.START, TestSystem.END), equalTo(Mass.grams(388.8)));

        TestStatisticDataSet<Mass> averageCarbonEmissions = statistics.average().carbonEmissions();
        assertThat(averageCarbonEmissions.between(TestSystem.START, TestSystem.END), equalTo(Mass.grams(416.4)));
    }

    @Test
    public void individualDistancePerTravelMode() {
        given();

        TestStatistics statistics = sarah.getStatistics();
        TestStatisticDataSet<Length> individualDistanceByCar = statistics.individual().distanceBy(TravelMode.CAR);
        assertThat(individualDistanceByCar.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(216)));

        TestStatisticDataSet<Length> individualDistanceByPublicTransit = statistics.individual().distanceBy(TravelMode.PUBLIC_TRANSIT);
        assertThat(individualDistanceByPublicTransit.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(144)));

        TestStatisticDataSet<Length> individualDistanceByBicycle = statistics.individual().distanceBy(TravelMode.BICYCLING);
        assertThat(individualDistanceByBicycle.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(156)));

        TestStatisticDataSet<Length> averageDistanceByCar = statistics.average().distanceBy(TravelMode.CAR);
        assertThat(averageDistanceByCar.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(296)));

        TestStatisticDataSet<Length> averageDistanceByPublicTransit = statistics.average().distanceBy(TravelMode.PUBLIC_TRANSIT);
        assertThat(averageDistanceByPublicTransit.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(68)));

        TestStatisticDataSet<Length> averageDistanceByBicycle = statistics.average().distanceBy(TravelMode.BICYCLING);
        assertThat(averageDistanceByBicycle.between(TestSystem.START, TestSystem.END), equalTo(Length.kilometers(112)));
    }
}
