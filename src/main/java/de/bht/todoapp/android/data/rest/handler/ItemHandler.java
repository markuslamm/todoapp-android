/**
 * 
 */
package de.bht.todoapp.android.data.rest.handler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
import de.bht.todoapp.android.model.TodoItem;

/**
 * @author markus
 *
 */
public class ItemHandler implements ResponseHandler<TodoItem>
{
	private static final String TAG = ItemHandler.class.getSimpleName();

	private ContentResolver contentResolver;

	public ItemHandler(final ContentResolver contentResolver)
	{
		this.contentResolver = contentResolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.rest.ResponseHandler#handleResponse(java.
	 * lang.Object)
	 */
	@Override
	public TodoItem handleResponse(final TodoItem response) {
		Log.d(TAG, "handle TodoItem response: " + response);
		final ContentValues values = TodoItem.initContentValues(response);

		/*
		 * is new item, insert
		 */
		if (null == response.getInternalId()) {
			final Uri uri = contentResolver.insert(TodoItemDescriptor.CONTENT_URI, values);
			final Long internalId = Long.valueOf(uri.getLastPathSegment());
			response.setInternalId(internalId);
			Log.d(TAG, "stored new item in local db: " + uri);
		}
		/*
		 * existing item, update
		 */
		else {
			final int updateCount = contentResolver.update(TodoItemDescriptor.CONTENT_URI, values, "_id=" + response.getInternalId(), null);
			Log.d(TAG, "Items updated: " + updateCount);
		}
		return response;
	}
}
