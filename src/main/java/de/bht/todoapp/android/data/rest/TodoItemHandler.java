/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import android.util.Log;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 *
 */
public class TodoItemHandler implements ResponseHandler<TodoItem>
{
	private static final String TAG = TodoItemHandler.class.getSimpleName();

	private BaseActivity context;

	public TodoItemHandler(final BaseActivity context)
	{
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see de.bht.todoapp.android.data.rest.ResponseHandler#handleResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public void handleResponse(TodoItem responseEntity) {
		Log.d(TAG, "handle Account response.");

		
	}

}
