package us.sustainify.web;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sustainify.common.domain.model.organisation.SustainifyUser;
import be.appify.framework.common.security.domain.SimpleCredential;
import be.appify.framework.security.domain.Authentication;
import be.appify.framework.security.service.AuthenticationService;
import be.appify.framework.security.service.EncryptionService;
import be.appify.framework.security.service.NotAuthenticatedException;

import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Post;

@At("/sign-in")
@Show("SignIn.html")
public class SignInPage {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignInPage.class);
	private final AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService;
	private String emailAddress;
	private String encryptedPassword;
	private final EncryptionService encryptionService;
	private String target = "/";
	private boolean failure;
	private final SessionContext sessionContext;
	private boolean keepSignedIn;

	@Inject
	public SignInPage(AuthenticationService<SustainifyUser, SimpleCredential<SustainifyUser>> authenticationService, EncryptionService encryptionService,
			SessionContext sessionContext) {
		this.authenticationService = authenticationService;
		this.encryptionService = encryptionService;
		this.sessionContext = sessionContext;
	}

	@Post
	public Reply<?> signIn() {
		try {
			SimpleCredential<SustainifyUser> credential = new SimpleCredential<SustainifyUser>(emailAddress, encryptedPassword);
			Authentication<SustainifyUser> authentication = authenticationService.authenticate(credential, keepSignedIn);
			sessionContext.setAuthentication(authentication);
			failure = false;
			return Reply.saying().redirect(target);
		} catch (NotAuthenticatedException e) {
			LOGGER.warn("Authentication failed for " + emailAddress);
			failure = true;
			return null;
		}
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.encryptedPassword = encryptionService.encrypt(password);
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public boolean isFailure() {
		return failure;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setKeepSignedIn(boolean keepSignedIn) {
		this.keepSignedIn = keepSignedIn;
	}

	public boolean isKeepSignedIn() {
		return keepSignedIn;
	}
}
