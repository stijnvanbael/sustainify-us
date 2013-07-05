package us.sustainify.commute.domain.service.google.directions;

import com.google.api.client.util.Key;

public class TransitTime {

	@Key
	private String text;

	@Key
	private Long value;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

}
