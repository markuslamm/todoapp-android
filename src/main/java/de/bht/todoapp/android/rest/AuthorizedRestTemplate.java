/**
 * 
 */
package de.bht.todoapp.android.rest;

import java.util.Collections;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author markus
 *
 */
public class AuthorizedRestTemplate
{
	private RestTemplate restTemplate;
	
	private String username;
	private String password;
	
	public AuthorizedRestTemplate(final String username, final String password)
	{
		this.username = username;
		this.password = password;
	}
	
	private RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(getMappingJacksonConverter());
		return restTemplate;
	}

	public HttpHeaders createAcceptHeader() {
		final HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
		final HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
		return requestHeaders;
	}
	
	private MappingJacksonHttpMessageConverter getMappingJacksonConverter() {
		final MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
		return messageConverter;
	}
}
