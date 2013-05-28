/**
 * 
 */
package de.bht.todoapp.android.data.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author markus
 * 
 */
public class TodoItemProvider extends ContentProvider
{
	private static final String TAG = TodoItemProvider.class.getSimpleName();

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Map<String, String> PROJECTION_MAP = new HashMap<String, String>();

	static {
		TodoItemProvider.URI_MATCHER.addURI(TodoItemDescriptor.AUTHORITY, TodoItemDescriptor.PATH_MULTIPLE, TodoItemDescriptor.ITEMS_CODE);
		TodoItemProvider.URI_MATCHER.addURI(TodoItemDescriptor.AUTHORITY, TodoItemDescriptor.PATH_SINGLE, TodoItemDescriptor.ITEM_CODE);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.ID_COLUMN, TodoItemDescriptor.ID_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.SERVERID_COLUMN, TodoItemDescriptor.SERVERID_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.TITLE_COLUMN, TodoItemDescriptor.TITLE_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.DESCRIPTION_COLUMN, TodoItemDescriptor.DESCRIPTION_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.STATUS_COLUMN, TodoItemDescriptor.STATUS_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.PRIORITY_COLUMN, TodoItemDescriptor.PRIORITY_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.DUEDATE_COLUMN, TodoItemDescriptor.DUEDATE_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.LATITUDE_COLUMN, TodoItemDescriptor.LATITUDE_COLUMN);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.LONGITUDE_COLUMN, TodoItemDescriptor.LONGITUDE_COLUMN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate()...");
		final DatabaseHelper dbHelper = new DatabaseHelper(getContext());
		db = dbHelper.getWritableDatabase();
		return db == null ? false : true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TodoItemDescriptor.TABLE_NAME);
		switch (TodoItemProvider.URI_MATCHER.match(uri)) {
			case TodoItemDescriptor.ITEMS_CODE:
				queryBuilder.setProjectionMap(TodoItemProvider.PROJECTION_MAP);
				break;
			case TodoItemDescriptor.ITEM_CODE:
				queryBuilder.appendWhere(TodoItemDescriptor.ID_COLUMN + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = TodoItemDescriptor.DEFAULT_SORT_ORDER;
		}
		else {
			orderBy = sortOrder;
		}
		final Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (TodoItemProvider.URI_MATCHER.match(uri) != TodoItemDescriptor.ITEMS_CODE) {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		}
		else {
			values = new ContentValues();
		}
		long rowId = db.insert(TodoItemDescriptor.TABLE_NAME, null, values);
		if (rowId > 0) {
			final Uri result = ContentUris.withAppendedId(TodoItemDescriptor.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(result, null);
			Log.d(TAG, String.format("ToDoItem created. [id:%d]", rowId));
			return result;
		}
		throw new SQLiteException("Unable to insert row into " + uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		switch (TodoItemProvider.URI_MATCHER.match(uri)) {
			case TodoItemDescriptor.ITEMS_CODE:
				count = db.update(TodoItemDescriptor.TABLE_NAME, values, selection, selectionArgs);
				break;
			case TodoItemDescriptor.ITEM_CODE:
				final String itemId = uri.getPathSegments().get(1);
				if (TextUtils.isEmpty(selection)) {
					count = db.update(TodoItemDescriptor.TABLE_NAME, values, TodoItemDescriptor.ID_COLUMN + "=" + itemId, null);
				}
				else {
					count = db.update(TodoItemDescriptor.TABLE_NAME, values, TodoItemDescriptor.ID_COLUMN + "=" + itemId + " and "
							+ selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (TodoItemProvider.URI_MATCHER.match(uri)) {
			case TodoItemDescriptor.ITEMS_CODE:
				count = db.delete(TodoItemDescriptor.TABLE_NAME, selection, selectionArgs);
				break;
			case TodoItemDescriptor.ITEM_CODE:
				final String itemId = uri.getPathSegments().get(1);
				if (TextUtils.isEmpty(selection)) {
					count = db.delete(TodoItemDescriptor.TABLE_NAME, TodoItemDescriptor.ID_COLUMN + "=" + itemId, null);
				}
				else {
					count = db.delete(TodoItemDescriptor.TABLE_NAME, TodoItemDescriptor.ID_COLUMN + "=" + itemId + " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (TodoItemProvider.URI_MATCHER.match(uri)) {
			case TodoItemDescriptor.ITEMS_CODE:
				return TodoItemDescriptor.MIME_TYPE_MULTIPLE;
			case TodoItemDescriptor.ITEM_CODE:
				return TodoItemDescriptor.MIME_TYPE_SINGLE;
			default:
				throw new IllegalArgumentException("Unknown type: " + uri);
		}
	}
}
