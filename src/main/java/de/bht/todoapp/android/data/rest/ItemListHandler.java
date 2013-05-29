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
import de.bht.todoapp.android.model.TodoItemList;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 *
 */
public class ItemListHandler implements ResponseHandler<TodoItemList>
{
	private static final String TAG = ItemListHandler.class.getSimpleName();
	private BaseActivity context;

	public ItemListHandler(final BaseActivity context)
	{
		this.context = context;
	}
	/* (non-Javadoc)
	 * @see de.bht.todoapp.android.data.rest.ResponseHandler#handleResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public TodoItemList handleResponse(final TodoItemList responseEntity) {
		Log.d(TAG, "handle TodoItemList response.");
		if (responseEntity.getItems().size() > 0) {
			// final Cursor localItems = ((Activity)
			// context).getContentResolver().query(TodoItemDescriptor.CONTENT_URI,
			// null, null, null, null);
			// final List<TodoItem> itemsToInsert = new ArrayList<TodoItem>();
			for (final TodoItem item : responseEntity.getItems()) {
				/* TODO check local stored items */
				final ContentValues values = TodoItem.initContentValues(item);
				final Uri uri = ((Activity) context).getContentResolver().insert(TodoItemDescriptor.CONTENT_URI, values);
				final Long internalId = Long.valueOf(uri.getLastPathSegment());
				item.setInternalId(internalId);
				Log.d(TAG, "stored list in local db: " + uri);
			}
		}
		return responseEntity;
	}


}
