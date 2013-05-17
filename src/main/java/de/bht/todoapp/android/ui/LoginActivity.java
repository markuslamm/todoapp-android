/**
 * 
 */
package de.bht.todoapp.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.bht.todoapp.android.AuthenticationException;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.ui.base.AbstractActivity;
import de.bht.todoapp.android.ui.base.AbstractAsyncTask;
import de.bht.todoapp.android.ui.base.BaseActivity;
import de.bht.todoapp.android.util.Validator;

/**
 * @author markus
 * 
 */
public class LoginActivity extends AbstractActivity
{
	private static final String TAG = LoginActivity.class.getSimpleName();

	private EditText txtEmail;
	private EditText txtPassword;
	private Button btnLogin;
	private AuthenticationTask authenticationTask = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		txtEmail = (EditText) findViewById(R.id.txt_email);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
	}

	/**
	 * @param view
	 */
	public void onClickLogin(final View view) {
		Log.d(TAG, "onClickLogin()..");
		if (authenticationTask != null) {
			return;
		}
		final String email = txtEmail.getText().toString();
		final String password = txtPassword.getText().toString();
		boolean hasErrors = false;
		if (!Validator.isValidEmail(email)) {
			txtEmail.setError(getString(R.string.error_email_format));
			hasErrors = true;
		}
		if (!Validator.isValidPassword(password)) {
			txtPassword.setError(getString(R.string.error_password_format));
			hasErrors = true;
		}
		if (hasErrors) {
			return;
		}
		/* No errors, authenticate */

		authenticationTask = new AuthenticationTask(this, getString(R.string.progress_authenticating));
		authenticationTask.execute(email, password);
	}

	private class AuthenticationTask extends AbstractAsyncTask<String, Void, Boolean>
	{
		private Boolean isAuthenticated = Boolean.FALSE;

		/**
		 * @param activity
		 * @param message
		 */
		public AuthenticationTask(final BaseActivity activity, final String message)
		{
			super(activity, message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			final String email = params[0];
			final String password = params[1];
			try {
				isAuthenticated = performAuthentication(email, password);
			}
			catch (AuthenticationException e) {
				Log.d(TAG, e.getMessage());
				return Boolean.FALSE;
			}
			return isAuthenticated;
		}

		private Boolean performAuthentication(final String user, final String password) throws AuthenticationException {
			Log.d(TAG, "performAuthentication()...");
			boolean authenticated = Boolean.FALSE;
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO
			// Populate the HTTP Basic Authentitcation header with the username
			// and password
			authenticated = (user.equals("111@web.de") && password.equals("111111")) ? Boolean.TRUE : Boolean.FALSE;
			if(!authenticated) {
				throw new AuthenticationException(String.format("Authentication failed! [%s] [%s]", user, password));
			}
			return authenticated;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * net.findme.android.ui.base.AbstractAsyncTask#onPostExecute(java.lang
		 * .Object)
		 */
		@Override
		protected void onPostExecute(final Boolean result) {
			super.onPostExecute(result);
			authenticationTask = null;
			if (result == Boolean.TRUE) { // Successfully authenticated
				final Intent intent = new Intent(LoginActivity.this, ItemListActivity.class);
				startActivity(intent);
				finish();
			}
			else {
				Log.d(TAG, "Authentication error!");
				getMainApplication().displayErrorAlert(LoginActivity.this, getString(R.string.error_title),
						getString(R.string.error_authorization));
			}
		}
	}
}
