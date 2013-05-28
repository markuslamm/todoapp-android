/**
 * 
 */
package de.bht.todoapp.android.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author markus
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context)
	{
		super(context, AccountDescriptor.DB_NAME, null, AccountDescriptor.DB_VERSION);
	}

	private void createAccountTable(final SQLiteDatabase db) {
		final String stmt = "CREATE TABLE " + AccountDescriptor.TABLE_NAME + " (" + AccountDescriptor.ID_COLUMN
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + AccountDescriptor.SERVERID_COLUMN + " INTEGER," + AccountDescriptor.EMAIL_COLUMN
				+ " TEXT NOT NULL," + AccountDescriptor.PASSWORD_COLUMN + " TEXT);";
		try {
			db.execSQL(stmt);
			Log.d(TAG, "table created:" + AccountDescriptor.TABLE_NAME);
		}
		catch (SQLiteException e) {
			Log.d(TAG, "Unable to create table " + AccountDescriptor.TABLE_NAME);
			throw e;
		}
	}

	private void createTodoItemsTable(final SQLiteDatabase db) {
		final String stmt = "CREATE TABLE " + TodoItemDescriptor.TABLE_NAME + " (" + TodoItemDescriptor.ID_COLUMN
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TodoItemDescriptor.SERVERID_COLUMN + " INTEGER,"
				+ TodoItemDescriptor.TITLE_COLUMN + " TEXT NOT NULL," + TodoItemDescriptor.DESCRIPTION_COLUMN + " TEXT,"
				+ TodoItemDescriptor.STATUS_COLUMN + " TEXT NOT NULL," + TodoItemDescriptor.PRIORITY_COLUMN + " TEXT NOT NULL,"
				+ TodoItemDescriptor.DUEDATE_COLUMN + " INTEGER," + TodoItemDescriptor.LATITUDE_COLUMN + " REAL,"
				+ TodoItemDescriptor.LONGITUDE_COLUMN + " REAL);";
		try {
			db.execSQL(stmt);
			Log.d(TAG, "table created:" + TodoItemDescriptor.TABLE_NAME);

		}
		catch (SQLiteException e) {
			Log.d(TAG, "Unable to create table " + TodoItemDescriptor.TABLE_NAME);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			createAccountTable(db);
			createTodoItemsTable(db);
			Log.d(TAG, String.format("Database [%s] created.", AccountDescriptor.DB_NAME));
		}
		catch (SQLiteException e) {
			Log.e(TAG, String.format("Unable to create database [%s]: %s", AccountDescriptor.DB_NAME, e.getMessage()));
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TodoItemDescriptor.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + AccountDescriptor.TABLE_NAME);
			createAccountTable(db);
			createTodoItemsTable(db);
			Log.d(TAG, String.format("Upgrading database [%s]from version %d to %d", AccountDescriptor.DB_NAME, oldVersion, newVersion));
		}
		catch (SQLiteException e) {
			Log.e(TAG, String.format("Unable to upgrade database [%s] from version %d to %d", AccountDescriptor.DB_NAME, oldVersion,
					newVersion));
		}
	}
}
