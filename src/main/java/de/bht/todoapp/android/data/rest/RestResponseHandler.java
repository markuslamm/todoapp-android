/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import org.springframework.http.ResponseEntity;

/**
 * @author markus
 *
 */
public interface RestResponseHandler
{
	void handleResponse(ResponseEntity<?> response);
}
