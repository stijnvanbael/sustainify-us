package us.sustainify.commute.domain.service.google.directions;

import javax.xml.bind.annotation.XmlElement;

import com.google.api.client.util.Key;

public class Numeric {
	@Key
	private int value;

	@XmlElement
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
