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
import de.bht.todoapp.android.model.TodoItemList;

/**
 * @author markus
 * 
 */
public class ItemListHandler implements ResponseHandler<TodoItemList>
{
	private static final String TAG = ItemListHandler.class.getSimpleName();

	private ContentResolver contentResolver;

	public ItemListHandler(final ContentResolver contentResolver)
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
	public TodoItemList handleResponse(final TodoItemList response) {
		Log.d(TAG, "handle TodoItemList response: " + response);
		if (response.getItems().size() > 0) {
			for (final TodoItem item : response.getItems()) {
				final ContentValues values = TodoItem.initContentValues(item);
				final Uri uri = contentResolver.insert(TodoItemDescriptor.CONTENT_URI, values);
				final Long internalId = Long.valueOf(uri.getLastPathSegment());
				item.setInternalId(internalId);
				Log.d(TAG, "stored item in local db: " + uri);
			}
		}
		return response;
	}

}
