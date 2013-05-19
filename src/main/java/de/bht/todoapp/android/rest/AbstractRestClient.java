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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author markus
 *
 */
public abstract class AbstractRestClient<T>
{
	private static final String TAG = AbstractRestClient.class.getSimpleName();
	private RestTemplate restTemplate;
	private Class<T> clazz;

	protected String serverRoot = "http://10.0.2.2:8080";
	protected String applicationPath = "/todoapp-server";
	protected String authenticationURI = "/authenticate";
	private static final String PATH_SEPARATOR = "/";
	
	private String email;
	private String password;

	public AbstractRestClient(final Class<T> clazz, final String email, final String password) {
		this.clazz = clazz;
		this.email = email;
		this.password = password;
	}
	
	public T findOneById(final Long entityId) {
		HttpAuthentication authHeader = new HttpBasicAuthentication(email, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		restTemplate = getRestTemplate();
		final ResponseEntity<T> response = restTemplate.exchange(getRequestURI() + PATH_SEPARATOR + entityId, HttpMethod.GET, requestEntity, clazz);
		return response.getBody();
	}
	
	protected abstract String getRequestURI();
	
	protected RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(getMappingJacksonConverter());
		return restTemplate;
	}

//	public HttpHeaders createAcceptHeader() {
//		final HttpHeaders requestHeaders = new HttpHeaders();
//		requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
//		return requestHeaders;
//	}
//	
//	public HttpHeaders createContentTypeHeader() {
//		final HttpHeaders requestHeaders = new HttpHeaders();
//		requestHeaders.setContentType(new MediaType("application", "json"));
//		return requestHeaders;
//	}

	private MappingJacksonHttpMessageConverter getMappingJacksonConverter() {
		final MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
		return messageConverter;
	}
	
	public String getServerRoot() {
		return serverRoot;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	protected String getContextPath() {
		return serverRoot + applicationPath;
	}

	protected String getAuthenticationURI() {
		return getContextPath() + authenticationURI;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
}
