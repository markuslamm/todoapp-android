/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import org.springframework.http.ResponseEntity;

import android.util.Log;
import de.bht.todoapp.android.model.TodoItem;

/**
 * @author markus
 *
 */
public class TodoItemHandler implements RestResponseHandler
{
	private static final String TAG = TodoItemHandler.class.getSimpleName();

	/* (non-Javadoc)
	 * @see de.bht.todoapp.android.data.rest.RestResponseHandler#handleResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public void handleResponse(final ResponseEntity<?> response) {
		if (response.getBody() instanceof TodoItem) {
            Log.d(TAG, "Handling TodoItem response");
        }		
	}

}
