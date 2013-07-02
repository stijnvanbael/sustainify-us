package us.sustainify.web.authenticated.widget;

import java.util.Map;

import us.sustainify.commute.domain.model.route.TravelMode;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

final class TravelModes {

	static final BiMap<TravelMode, String> TRAVEL_MODE_IDS = HashBiMap.create();
	static final BiMap<TravelMode, String> TRAVEL_MODES = HashBiMap.create();
	static final Map<TravelMode, String> ROUTE_DESCRIPTIONS = Maps.newHashMap();
	static final Map<TravelMode, String> SUBROUTE_DESCRIPTIONS = Maps.newHashMap();

	static {
		TRAVEL_MODE_IDS.put(TravelMode.BICYCLING, "bicycling");
		TRAVEL_MODE_IDS.put(TravelMode.CAR, "car");
		TRAVEL_MODE_IDS.put(TravelMode.PUBLIC_TRANSIT, "publicTransit");
		TRAVEL_MODE_IDS.put(TravelMode.WALKING, "walking");

		TRAVEL_MODES.put(TravelMode.BICYCLING, "bicycling");
		TRAVEL_MODES.put(TravelMode.CAR, "driving");
		TRAVEL_MODES.put(TravelMode.PUBLIC_TRANSIT, "transit");
		TRAVEL_MODES.put(TravelMode.WALKING, "walking");

		ROUTE_DESCRIPTIONS.put(TravelMode.BICYCLING, "by bicycle");
		ROUTE_DESCRIPTIONS.put(TravelMode.CAR, "by car");
		ROUTE_DESCRIPTIONS.put(TravelMode.PUBLIC_TRANSIT, "by public transit");
		ROUTE_DESCRIPTIONS.put(TravelMode.WALKING, "walking");

		SUBROUTE_DESCRIPTIONS.put(TravelMode.BICYCLING, "Bicycling");
		SUBROUTE_DESCRIPTIONS.put(TravelMode.CAR, "Car");
		SUBROUTE_DESCRIPTIONS.put(TravelMode.PUBLIC_TRANSIT, "Public transit");
		SUBROUTE_DESCRIPTIONS.put(TravelMode.WALKING, "Walking");
	}
}
