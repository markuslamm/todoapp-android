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
public class AccountProvider extends ContentProvider
{
	private static final String TAG = AccountProvider.class.getSimpleName();

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Map<String, String> PROJECTION_MAP = new HashMap<String, String>();

	static {
		AccountProvider.URI_MATCHER.addURI(AccountDescriptor.AUTHORITY, AccountDescriptor.PATH_MULTIPLE, AccountDescriptor.ACCOUNTS_CODE);
		AccountProvider.URI_MATCHER.addURI(AccountDescriptor.AUTHORITY, AccountDescriptor.PATH_SINGLE, AccountDescriptor.ACCOUNT_CODE);
		AccountProvider.PROJECTION_MAP.put(AccountDescriptor.ID_COLUMN, AccountDescriptor.ID_COLUMN);
		AccountProvider.PROJECTION_MAP.put(AccountDescriptor.SERVERID_COLUMN, AccountDescriptor.SERVERID_COLUMN);
		AccountProvider.PROJECTION_MAP.put(AccountDescriptor.EMAIL_COLUMN, AccountDescriptor.EMAIL_COLUMN);
		AccountProvider.PROJECTION_MAP.put(AccountDescriptor.PASSWORD_COLUMN, AccountDescriptor.PASSWORD_COLUMN);
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
		queryBuilder.setTables(AccountDescriptor.TABLE_NAME);
		switch (AccountProvider.URI_MATCHER.match(uri)) {
			case AccountDescriptor.ACCOUNTS_CODE:
				queryBuilder.setProjectionMap(AccountProvider.PROJECTION_MAP);
				break;
			case AccountDescriptor.ACCOUNT_CODE:
				queryBuilder.appendWhere(AccountDescriptor.ID_COLUMN + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = AccountDescriptor.DEFAULT_SORT_ORDER;
		}
		else {
			orderBy = sortOrder;
		}
		final Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (AccountProvider.URI_MATCHER.match(uri) != AccountDescriptor.ACCOUNTS_CODE) {
			throw new IllegalArgumentException("Unknown Account URI: " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		}
		else {
			values = new ContentValues();
		}
		long rowId = db.insert(AccountDescriptor.TABLE_NAME, null, values);
		if (rowId > 0) {
			final Uri result = ContentUris.withAppendedId(AccountDescriptor.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(result, null);
			Log.d(TAG, String.format("Account created. [id:%d]", rowId));
			return result;
		}
		throw new SQLiteException("Unable to insert account row into " + uri);
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
		switch (AccountProvider.URI_MATCHER.match(uri)) {
			case AccountDescriptor.ACCOUNTS_CODE:
				count = db.update(AccountDescriptor.TABLE_NAME, values, selection, selectionArgs);
				break;
			case AccountDescriptor.ACCOUNT_CODE:
				final String accountId = uri.getPathSegments().get(1);
				if (TextUtils.isEmpty(selection)) {
					count = db.update(AccountDescriptor.TABLE_NAME, values, AccountDescriptor.ID_COLUMN + "=" + accountId, null);
				}
				else {
					count = db.update(AccountDescriptor.TABLE_NAME, values, AccountDescriptor.ID_COLUMN + "=" + accountId + " and "
							+ selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown Account URI: " + uri);
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
		switch (AccountProvider.URI_MATCHER.match(uri)) {
			case AccountDescriptor.ACCOUNTS_CODE:
				count = db.delete(AccountDescriptor.TABLE_NAME, selection, selectionArgs);
				break;
			case AccountDescriptor.ACCOUNT_CODE:
				final String accountId = uri.getPathSegments().get(1);
				if (TextUtils.isEmpty(selection)) {
					count = db.delete(AccountDescriptor.TABLE_NAME, AccountDescriptor.ID_COLUMN + "=" + accountId, null);
				}
				else {
					count = db.delete(AccountDescriptor.TABLE_NAME, AccountDescriptor.ID_COLUMN + "=" + accountId + " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown Account URI: " + uri);
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
		switch (AccountProvider.URI_MATCHER.match(uri)) {
			case AccountDescriptor.ACCOUNTS_CODE:
				return AccountDescriptor.MIME_TYPE_MULTIPLE;
			case AccountDescriptor.ACCOUNT_CODE:
				return AccountDescriptor.MIME_TYPE_SINGLE;
			default:
				throw new IllegalArgumentException("Unknown type: " + uri);
		}
	}
}
