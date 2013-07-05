package us.sustainify.commute.service.google.places;

import static be.appify.framework.test.util.Asserts.assertContains;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import us.sustainify.commute.domain.model.place.Place;
import us.sustainify.commute.domain.model.place.PlaceType;
import us.sustainify.commute.domain.service.google.places.GooglePlaceService;
import be.appify.framework.location.domain.Location;
import be.appify.framework.test.service.WebServiceClientTest;

import com.google.api.client.http.apache.ApacheHttpTransport;

public class GooglePlaceServiceTest extends WebServiceClientTest {
	private GooglePlaceService placeService;
	private Location location;

	@Before
	public void before() {
		location = new Location("Ruddervoorde", 51.09601, 3.206581);
		placeService = new GooglePlaceService(new ApacheHttpTransport(), "API-KEY");
		placeService.setPlaceURL("http://localhost:" + PORT + "/maps/api/place/nearbysearch/json");
		addStubResponse("/maps/api/place/nearbysearch/json\\?.*types=train_station.*",
				"/us/sustainify/commute/service/google/places/railway-stations.json");
	}

	@Test
	public void testFindPlacesNearby() {
		Set<Place> places = placeService.findPlacesNearby(location, PlaceType.RAILWAY_STATION, 15000);
		assertEquals(10, places.size());

		assertContains(places, new Place(new Location(51.1976710, 3.2178510), "Station Brugge", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.223106, 3.201837), "Station Brugge-Sint-Pieters", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(50.990583, 3.32963), "Station Tielt", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.025, 3.04321), "Kortemark", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.154229, 3.257349), "Station Oostkamp", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.126584, 3.163808), "Station Zedelgem", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.06482, 3.105509), "Station Torhout", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.02507, 3.127427), "Station Lichtervelde", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(51.128124, 3.329258), "Station Beernem", PlaceType.RAILWAY_STATION));
		assertContains(places, new Place(new Location(50.984604, 3.418327), "Station Aarsele", PlaceType.RAILWAY_STATION));
	}

}
