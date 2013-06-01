/**
 * 
 */
package de.bht.todoapp.android.data.rest.handler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;
import de.bht.todoapp.android.data.db.AccountDescriptor;
import de.bht.todoapp.android.model.Account;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 * 
 */
public class AccountHandler implements ResponseHandler<Account>
{
	private static final String TAG = AccountHandler.class.getSimpleName();

	private BaseActivity context;
	private ContentResolver contentResolver;

	public AccountHandler(final BaseActivity context, final ContentResolver contentResolver)
	{
		this.context = context;
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
	public Account handleResponse(final Account response) {
		Log.d(TAG, "handle Account response: " + response);
		/*
		 * prepare for local db store
		 */
		final ContentValues values = Account.initContentValues(response);
		/*
		 * insert in db
		 */
		final Uri uri = contentResolver.insert(AccountDescriptor.CONTENT_URI, values);
		final Long internalId = Long.valueOf(uri.getLastPathSegment());
		response.setInternalId(internalId);
		Log.d(TAG, "Account saved in local store: " + uri);
		/*
		 * store account data in preferences store, needed for upcoming rest
		 * calls
		 */
		final Editor edit = context.getMainApplication().getPreferences().edit();
		edit.putLong("accountId", response.getEntityId());
		edit.putString("email", response.getEmail());
		edit.putString("password", response.getPassword());
		edit.commit();
		return response;
	}
}
