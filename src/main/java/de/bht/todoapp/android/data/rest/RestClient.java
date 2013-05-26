/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import java.util.Collections;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.model.Account;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;

/**
 * @author markus
 *
 */
public class RestClient
{
	private static final String TAG = RestClient.class.getSimpleName();

	//protected String SERVER_ROOT = "http://10.0.2.2:8080";
	private static final String SERVER_ROOT = "http://192.168.2.100:8080";

	private static final String APPLICATION_PATH = "/todoapp-server";
	private static final String AUTH_PATH = "/authenticate";
	private static final String ITEMS_URI = "/items";

	private static final String PATH_SEPARATOR = "/";
	
	private String email;
	private String password;
	
	private static RestClient instance = null;
	
	private RestClient(final String email, final String password)
	{
		this.email = email;
		this.password = password;
	}
	
	public static synchronized RestClient getInstance(final String email, final String password) {
		if(instance == null) {
			instance = new RestClient(email, password);
		}
		return instance;
	}
	
	public Account authenticateUser() throws AuthenticationException {
		final HttpEntity<?> requestEntity = getAuthorizedGetRequestEntity();
		RestTemplate restTemplate = getRestTemplate();
		Log.d(TAG, "Trying to authenticate with " + email + ":" + password);
		ResponseEntity<Account> response = null;
		// Make the network request
		try {
			response = restTemplate.exchange(getAuthURI(), HttpMethod.GET, requestEntity, Account.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				final Account accountData = response.getBody();
				Log.d(TAG, "Response body: " + accountData);
			}
		}
		catch (HttpClientErrorException e) {
			Log.d(TAG, "Unable to authenticate: " + e.getMessage());
			throw new AuthenticationException("Unable to authenticate: " + e.getMessage());
		}
		return response.getBody();
	}
	
	public TodoItemList findAllItems(final Long accountId) {
		final HttpEntity<Object> requestEntity = getAuthorizedGetRequestEntity();
		RestTemplate restTemplate = getRestTemplate();
		ResponseEntity<TodoItemList> response = null;
		try {
			response = restTemplate.exchange(getRequestURI(accountId), HttpMethod.GET, requestEntity, TodoItemList.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				final TodoItemList data = response.getBody();
				Log.d(TAG, "Response body: " + data);
			}
		}
		catch(HttpClientErrorException e) {
			Log.d(TAG, "Unable to fetch items: " + e.getMessage());
		}
		return response.getBody();
	}
	
	
//	public TodoItem createItem(final TodoItem item) {
//		
//	}
//	
//	public TodoItem updateItem(final TodoItem item) {
//		
//	}
//	
//	public TodoItem deleteItem(final TodoItem item) {
//		
//	}
//	
	
	
	protected HttpEntity<Object> getAuthorizedGetRequestEntity() {
		HttpAuthentication authHeader = new HttpBasicAuthentication("111@web.de", "111111");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
		return requestEntity;
	}
	
	protected HttpEntity<TodoItem> getAuthorizedPostRequestEntity(final String email, final String password, final TodoItem resource) {
		HttpAuthentication authHeader = new HttpBasicAuthentication(email, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<TodoItem> requestEntity = new HttpEntity<TodoItem>(resource, requestHeaders);
		return requestEntity;
	}
	
	private String getRequestURI(final Long accountId) {
		return SERVER_ROOT + APPLICATION_PATH + PATH_SEPARATOR + String.valueOf(accountId) + ITEMS_URI;
	}
	
	private String getAuthURI() {
		return SERVER_ROOT + APPLICATION_PATH + AUTH_PATH + PATH_SEPARATOR;
	}
	
	private RestTemplate getRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(getMappingJacksonConverter());
		return restTemplate;
	}

	private MappingJacksonHttpMessageConverter getMappingJacksonConverter() {
		final MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
		return messageConverter;
	}
}
