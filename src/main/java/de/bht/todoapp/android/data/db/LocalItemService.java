/**
 * 
 */
package de.bht.todoapp.android.data.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import de.bht.todoapp.android.data.ItemService;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;

/**
 * @author markus
 *
 */
public class LocalItemService implements ItemService
{
	private static final String TAG = LocalItemService.class.getSimpleName();

	private final ContentResolver contentResolver;

	public LocalItemService(final ContentResolver contentResolver)
	{
		this.contentResolver = contentResolver;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.data.ItemService#findAllItems()
	 */
	@Override
	public TodoItemList findAllItems() {
		final List<TodoItem> list = new ArrayList<TodoItem>();
		final Cursor cursor = contentResolver.query(TodoItemDescriptor.CONTENT_URI, null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				final TodoItem item = new TodoItem();
				item.setInternalId(cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.ID_COLUMN)));
				item.setEntityId(cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.SERVERID_COLUMN)));
				item.setTitle(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.DESCRIPTION_COLUMN)));
				item.setLatitude(cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LATITUDE_COLUMN)));
				item.setLongitude(cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LONGITUDE_COLUMN)));
				item.setDueDate(cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN)));
				item.setPriority(TodoItem.getPriorityFromString(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.PRIORITY_COLUMN))));
				item.setStatus(TodoItem.getStatusFromString(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.STATUS_COLUMN))));
				list.add(item);
			}
			cursor.close();
		}
		return new TodoItemList(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#createItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem createItem(final TodoItem item) {
		final ContentValues values = TodoItem.initContentValues(item);
		final Uri uri = contentResolver.insert(TodoItemDescriptor.CONTENT_URI, values);
		final Long internalId = Long.valueOf(uri.getLastPathSegment());
		item.setInternalId(internalId);
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#updateItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public TodoItem updateItem(final TodoItem item) {
		final ContentValues values = TodoItem.initContentValues(item);
		final Uri uri = ContentUris.withAppendedId(TodoItemDescriptor.CONTENT_URI, item.getInternalId());
		final int updateCount = contentResolver.update(uri, values, null, null);
		Log.d(TAG, updateCount + " item locally updated. " + item);
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#deleteItem(de.bht.todoapp.android
	 * .model.TodoItem)
	 */
	@Override
	public int deleteItem(final TodoItem item) {
		final Uri uri = ContentUris.withAppendedId(TodoItemDescriptor.CONTENT_URI, item.getInternalId());
		final int deleteCount = contentResolver.delete(uri, null, null);
		Log.d(TAG, deleteCount + " item locally deleted. " + item);
		return deleteCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.ItemService#createItemList(de.bht.todoapp
	 * .android.model.TodoItemList)
	 */
	@Override
	public TodoItemList createItemList(TodoItemList list) {
		throw new UnsupportedOperationException("not implemented yet.");
	}

}
