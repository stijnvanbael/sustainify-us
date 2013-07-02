package us.sustainify.commute.domain.service.google.places;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.commute.domain.model.place.Place;
import us.sustainify.commute.domain.model.place.PlaceType;
import us.sustainify.commute.domain.service.PlaceService;
import be.appify.framework.common.service.AbstractServiceClient;
import be.appify.framework.location.domain.Location;

import com.google.api.client.http.HttpTransport;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GooglePlaceService extends AbstractServiceClient implements PlaceService {
	private static final BiMap<PlaceType, String> TYPE_MAP = HashBiMap.create();
	private static final Logger LOGGER = LoggerFactory.getLogger(GooglePlaceService.class);

	static {
		TYPE_MAP.put(PlaceType.RAILWAY_STATION, "train_station");
	}

	private String placeURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private final String apiKey;

	public GooglePlaceService(HttpTransport transport, String apiKey) {
		super(transport);
		this.apiKey = apiKey;
	}

	public void setPlaceURL(String placeURL) {
		this.placeURL = placeURL;
	}

	@Override
	public Set<Place> findPlacesNearby(Location location, PlaceType type, int radiusInMeters) {
		Map<String, String> parameters = Maps.newHashMap();
		parameters.put("location", location.getLatitude() + "," + location.getLongitude());
		parameters.put("radius", Integer.toString(radiusInMeters));
		parameters.put("types", TYPE_MAP.get(type));
		parameters.put("sensor", "false");
		parameters.put("key", apiKey);
		PlaceMessage message = callService(PlaceMessage.class, placeURL, parameters);
		return convert(message, parameters);
	}

	private Set<Place> convert(PlaceMessage message, Map<String, String> parameters) {
		Set<Place> places = Sets.newHashSet();
		if ("OK".equals(message.getStatus())) {
			for (PlaceResult result : message.getResults()) {
				places.add(convert(result));
			}
		} else if (!"ZERO_RESULTS".equals(message.getStatus())) {
			LOGGER.error("Error finding places: " + message.getStatus() + " for " + constructURL(placeURL, parameters));
		}
		return places;
	}

	private Place convert(PlaceResult result) {
		PlaceLocation loc = result.getGeometry().getLocation();
		String name = result.getName();
		Location location = new Location(name, loc.getLat(), loc.getLng());
		Set<PlaceType> types = Sets.newHashSet();
		for (String type : result.getTypes()) {
			PlaceType placeType = TYPE_MAP.inverse().get(type);
			if (placeType != null) {
				types.add(placeType);
			}
		}
		return new Place(location, name, types);
	}

}
