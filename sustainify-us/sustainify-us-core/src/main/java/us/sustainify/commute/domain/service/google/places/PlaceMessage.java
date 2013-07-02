package us.sustainify.commute.domain.service.google.places;

import java.util.Set;

import com.google.api.client.util.Key;

public class PlaceMessage {
	@Key
	private Set<PlaceResult> results;

	@Key
	private String status;

	public Set<PlaceResult> getResults() {
		return results;
	}

	public void setResults(Set<PlaceResult> results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
