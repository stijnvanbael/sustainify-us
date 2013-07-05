package us.sustainify.web.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import us.sustainify.web.SessionContext;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.security.domain.Authentication;
import be.appify.framework.security.service.AuthenticationService;
import be.appify.framework.security.service.NotAuthenticatedException;

import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class AuthenticationFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	private final Injector injector;
	private final AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService;

	@Inject
	private AuthenticationFilter(Injector injector, AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService) {
		this.injector = injector;
		this.authenticationService = authenticationService;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		SessionContext sessionContext = injector.getInstance(SessionContext.class);
		Authentication<SustainifyUser> authentication = sessionContext.getAuthentication();
		if (authentication == null) {
			try {
				authentication = authenticationService.autoAuthenticate();
			} catch (NotAuthenticatedException e) {
				LOGGER.info(e.getMessage());
			}
		}
		if (authentication != null) {
			sessionContext.setAuthentication(authentication);
			chain.doFilter(request, response);
		} else {
			String requestURI = ((HttpServletRequest) request).getRequestURI();
			((HttpServletResponse) response).sendRedirect(authenticationService.getSignInURL(requestURI));
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
