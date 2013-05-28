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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.data.IDataAccessor;
import de.bht.todoapp.android.model.Account;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;

/**
 * @author markus
 * 
 */
public class RestClient implements IDataAccessor
{
	private static final String TAG = RestClient.class.getSimpleName();

	private static final String APPLICATION_PATH = "/todoapp-server";
	private static final String AUTH_PATH = "/authenticate";
	private static final String ITEMS_URI = "/items";

	// protected String SERVER_ROOT = "http://10.0.2.2:8080";
	private static final String SERVER_ROOT = "http://192.168.2.100:8080";

	private String email;
	private String password;

	public RestClient(final String email, final String password)
	{
		this.email = email;
		this.password = password;
	}

	public Account authenticateUser() throws AuthenticationException {
		final HttpHeaders requestHeaders = createAuthorizedHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		final RestTemplate restTemplate = getRestTemplate();
		Log.d(TAG, "Trying to authenticate with " + email + ":" + password);
		Account accountData = null;
		try {
			ResponseEntity<Account> response = restTemplate.exchange(SERVER_ROOT + APPLICATION_PATH + AUTH_PATH, HttpMethod.GET,
					requestEntity, Account.class);
			Log.d(TAG, "Authentication response: " + response.toString());

			if (response.getStatusCode() == HttpStatus.OK) {
				accountData = response.getBody();
				Log.d(TAG, "Response body: " + accountData);
			}
		}
		catch (RestClientException e) {
			Log.d(TAG, "Unable to authenticate: " + e.getMessage(), e);
			throw new AuthenticationException("Unable to authenticate: " + e.getMessage());
		}
		return accountData;
	}

	public TodoItemList findAllItems() {
		final HttpHeaders requestHeaders = createAuthorizedHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		final RestTemplate restTemplate = getRestTemplate();
		TodoItemList itemList = null;
		try {
			ResponseEntity<TodoItemList> response = restTemplate.exchange(SERVER_ROOT + APPLICATION_PATH + ITEMS_URI, HttpMethod.GET,
					requestEntity, TodoItemList.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				itemList = response.getBody();
				Log.d(TAG, "Response body: " + itemList);
			}
			Log.d(TAG, "TodoItemList response: " + response.toString());
		}
		catch (RestClientException e) {
			Log.d(TAG, "Unable to fetch items. " + e.getMessage(), e);
		}
		return itemList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.IDataAccessor#createItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem createItem(final TodoItem item) {
		final HttpHeaders requestHeaders = createAuthorizedHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<TodoItem> requestEntity = new HttpEntity<TodoItem>(item, requestHeaders);
		final RestTemplate restTemplate = getRestTemplate();
		TodoItem newItem = null;
		try {
			ResponseEntity<TodoItem> response = restTemplate.exchange(SERVER_ROOT + APPLICATION_PATH + ITEMS_URI, HttpMethod.POST,
					requestEntity, TodoItem.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				newItem = response.getBody();
				Log.d(TAG, "Response body: " + item);
			}
		}
		catch (RestClientException e) {
			Log.d(TAG, "Unable to create item: " + e.getMessage());
		}
		return newItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.IDataAccessor#updateItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem updateItem(TodoItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.data.IDataAccessor#deleteItem()
	 */
	@Override
	public int deleteItem() {
		// TODO Auto-generated method stub
		return 0;
	}

	private HttpHeaders createAuthorizedHeaders() {
		final HttpAuthentication auth = new HttpBasicAuthentication(email, password);
		final HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(auth);
		return requestHeaders;
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

	// public T updateRessource(final T ressource, final Long ressourceId) {
	// Log.d(TAG, "Update ressource with id: " + ressourceId);
	// final HttpHeaders requestHeaders = createAcceptHeader();
	// requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	// final HttpEntity<T> requestEntity = new HttpEntity<T>(ressource,
	// requestHeaders);
	// restTemplate = getRestTemplate();
	// final ResponseEntity<T> response = restTemplate.exchange(getURI() +
	// PATH_SEPARATOR + ressourceId , HttpMethod.PUT, requestEntity, clazz);
	// return response.getBody();
	// }

}
