package us.sustainify.commute.domain.model.notification;

public class Notification {
	private final String message;
	private final Object payload;
	private final Object sender;

	public Notification(String message, Object payload, Object sender) {
		this.message = message;
		this.payload = payload;
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public Object getPayload() {
		return payload;
	}

	public Object getSender() {
		return sender;
	}
}
