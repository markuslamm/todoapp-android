/**
 * 
 */
package de.bht.todoapp.android.data;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteDatabase;
import de.bht.todoapp.android.data.rest.RestResponseHandler;

/**
 * @author markus
 *
 */
public abstract class AbstractRestContentProvider extends ContentProvider
{
	private static final String TAG = AbstractRestContentProvider.class.getSimpleName();
	
	protected abstract SQLiteDatabase getDatabase();
	protected abstract RestResponseHandler newResponseHandler();

}
