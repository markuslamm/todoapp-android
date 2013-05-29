/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
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
	public TodoItem handleResponse(TodoItem responseEntity) {
		Log.d(TAG, "handle Account response.");
		final ContentValues values = TodoItem.initContentValues(responseEntity);
		final Uri uri = ((Activity) context).getContentResolver().insert(TodoItemDescriptor.CONTENT_URI, values);
		final Long internalId = Long.valueOf(uri.getLastPathSegment());
		responseEntity.setInternalId(internalId);
		Log.d(TAG, "stored item in local db: " + responseEntity);
		return responseEntity;
	}

}
