/**
 * 
 */
package de.bht.todoapp.android.rest;

import java.util.Collections;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.model.Account;

/**
 * @author markus
 * 
 */
public class AuthenticationRestClient extends AbstractRestClient<Account>
{
	private static final String TAG = AuthenticationRestClient.class.getSimpleName();
	private String email;
	private String password;

	// private static final String AUTHENTICATION_URI = "/authenticate";

	public AuthenticationRestClient(final String email, final String password)
	{
		super(Account.class, email, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.rest.AbstractRestClient#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		return authenticationURI;
	}

	public Account authenticateUser(final String email, final String password) throws AuthenticationException {
		HttpAuthentication authHeader = new HttpBasicAuthentication(email, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		// Create a new RestTemplate instance
		RestTemplate restTemplate = getRestTemplate();
		Log.d(TAG, "Trying to authenticate with " + email + ":" + password);
		ResponseEntity<Account> response = null;
		// Make the network request
		try {
			response = restTemplate.exchange(getAuthenticationURI(), HttpMethod.GET, requestEntity, Account.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				final Account accountData = response.getBody();
				Log.d(TAG, "Response body: " + accountData);
			}
		}
		catch (HttpClientErrorException e) {
			Log.d(TAG, "Unable to authenticate: " + e.getMessage());
			throw new AuthenticationException("Unable to authenticate");
		}
		return response.getBody();
	}
}
