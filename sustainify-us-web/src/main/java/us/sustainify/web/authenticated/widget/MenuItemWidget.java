package us.sustainify.web.authenticated.widget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.sitebricks.Show;
import com.google.sitebricks.rendering.EmbedAs;

@EmbedAs("MenuItem")
@Show("MenuItem.html")
public class MenuItemWidget {
	private String uri;
	private String caption;
	private String requestURI;

	@Inject
	public void setRequest(HttpServletRequest request) {
		requestURI = request.getRequestURI();
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getUri() {
		return uri;
	}

	public String getCaption() {
		return caption;
	}

	public boolean isActive() {
		return uri.equals(requestURI);
	}
}
