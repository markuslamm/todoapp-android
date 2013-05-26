/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import org.springframework.http.ResponseEntity;

import android.util.Log;
import de.bht.todoapp.android.model.Account;

/**
 * @author markus
 *
 */
public class TodoItemListHandler implements RestResponseHandler
{
	private static final String TAG = TodoItemListHandler.class.getSimpleName();

	/* (non-Javadoc)
	 * @see de.bht.todoapp.android.data.rest.RestResponseHandler#handleResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public void handleResponse(ResponseEntity<?> response) {
		if (response.getBody() instanceof Account) {
            Log.d(TAG, "Handling TodoItemList response");
        }
	}
}
