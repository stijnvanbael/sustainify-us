package us.sustainify.web.authenticated;

import be.appify.framework.security.domain.User;

public class UserViewModel {
	private final User user;

	public UserViewModel(User user) {
		this.user = user;
	}

	public String getFirstName() {
		return user.getFirstName();
	}

	public String getLastName() {
		return user.getLastName();
	}

}
