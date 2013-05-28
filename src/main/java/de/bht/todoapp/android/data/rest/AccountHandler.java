/**
 * 
 */
package de.bht.todoapp.android.data.rest;

import android.app.Activity;
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

	public AccountHandler(final BaseActivity context)
	{
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.data.rest.ResponseHandler#handleResponse(org.
	 * springframework.http.ResponseEntity)
	 */
	@Override
	public void handleResponse(final Account response) {
		Log.d(TAG, "handle Account response : " + response);
		/* store authenticated account in local sqlite store */
		final ContentValues values = new ContentValues();
		values.put(AccountDescriptor.SERVERID_COLUMN, response.getEntityId());
		values.put(AccountDescriptor.EMAIL_COLUMN, response.getEmail());
		values.put(AccountDescriptor.PASSWORD_COLUMN, response.getPassword());
		final Uri uri = ((Activity) context).getContentResolver().insert(AccountDescriptor.CONTENT_URI, values);
		Log.d(TAG, "Account saved in local store: " + response);
		/*
		 * store account data in preferences store, needed for upcoming rest
		 * calls
		 */
		final Editor edit = context.getMainApplication().getPreferences().edit();
		edit.putLong("accountId", response.getEntityId());
		edit.putString("email", response.getEmail());
		edit.putString("password", response.getPassword());
		edit.commit();
	}
}
