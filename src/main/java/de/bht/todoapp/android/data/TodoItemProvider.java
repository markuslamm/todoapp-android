/**
 * 
 */
package de.bht.todoapp.android.data;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import de.bht.todoapp.android.data.rest.RestResponseHandler;
import de.bht.todoapp.android.data.rest.TodoItemHandler;

/**
 * @author markus
 * 
 */
public class TodoItemProvider extends AbstractRestContentProvider
{
	private static final String TAG = TodoItemProvider.class.getSimpleName();

	private TodoItemDBHelper dbHelper;
	private SQLiteDatabase db;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Map<String, String> PROJECTION_MAP = new HashMap<String, String>();

	private boolean istNetworkAvailable = true;

	static {
		TodoItemProvider.URI_MATCHER.addURI(TodoItemDescriptor.AUTHORITY, TodoItemDescriptor.PATH_MULTIPLE, TodoItemDescriptor.ITEMS_CODE);
		TodoItemProvider.URI_MATCHER.addURI(TodoItemDescriptor.AUTHORITY, TodoItemDescriptor.PATH_SINGLE, TodoItemDescriptor.ITEM_CODE);
		TodoItemProvider.PROJECTION_MAP.put(TodoItemDescriptor.ID_COLUMN, TodoItemDescriptor.ID_COLUMN);
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
		final TodoItemDBHelper dbHelper = new TodoItemDBHelper(getContext());
		db = dbHelper.getWritableDatabase();
		return db == null ? false : true;
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
	
	 /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
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

    /* (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.AbstractRestContentProvider#getDatabase()
	 */
	@Override
	protected SQLiteDatabase getDatabase() {
		return db;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.data.AbstractRestContentProvider#newResponseHandler
	 * ()
	 */
	@Override
	protected RestResponseHandler newResponseHandler() {
		return new TodoItemHandler(this);
	}

	private static class TodoItemDBHelper extends SQLiteOpenHelper
	{
		private static final String TAG = TodoItemDBHelper.class.getSimpleName();

		public TodoItemDBHelper(Context context)
		{
			super(context, TodoItemDescriptor.DB_NAME, null, TodoItemDescriptor.DB_VERSION);
		}

		private void createTable(SQLiteDatabase db) {
			final String stmt = "CREATE TABLE " + TodoItemDescriptor.TABLE_NAME + " (" + TodoItemDescriptor.ID_COLUMN
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TodoItemDescriptor.SERVERID_COLUMN + " INTEGER,"
					+ TodoItemDescriptor.TITLE_COLUMN + " TEXT NOT NULL," + TodoItemDescriptor.DESCRIPTION_COLUMN + " TEXT,"
					+ TodoItemDescriptor.STATUS_COLUMN + " TEXT NOT NULL," + TodoItemDescriptor.PRIORITY_COLUMN + " TEXT NOT NULL,"
					+ TodoItemDescriptor.DUEDATE_COLUMN + " INTEGER," + TodoItemDescriptor.LATITUDE_COLUMN + " REAL,"
					+ TodoItemDescriptor.LONGITUDE_COLUMN + " REAL);";
			db.execSQL(stmt);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
		 * .sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				createTable(db);
				Log.d(TAG, String.format("Database [%s] created.", TodoItemDescriptor.DB_NAME));
			}
			catch (Exception e) {
				Log.e(TAG, String.format("Unable to create database [%s]!", TodoItemDescriptor.DB_NAME));
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + TodoItemDescriptor.TABLE_NAME);
				createTable(db);
				Log.d(TAG,
						String.format("Upgrading database [%s]from version %d to %d", new Object[] { TodoItemDescriptor.TABLE_NAME,
								oldVersion, newVersion }));
			}
			catch (Exception e) {
				Log.e(TAG,
						String.format("Unable to upgrade database [%s] from version %d to %d", new Object[] {
								TodoItemDescriptor.TABLE_NAME, oldVersion, newVersion }));
			}
		}
	}

}
