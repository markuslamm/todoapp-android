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
import de.bht.todoapp.android.model.Account;
import de.bht.todoapp.android.rest.AuthenticationRestClient;
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

	private class AuthenticationTask extends AbstractAsyncTask<String, Void, Account>
	{
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
		protected Account doInBackground(String... params) {
			final String email = params[0];
			final String password = params[1];
			Account authenticatedUser = null;
			try {
				authenticatedUser = performAuthentication(email, password);
			}
			catch (AuthenticationException e) {
				Log.d(TAG, e.getMessage());
			}
			return authenticatedUser;
		}

		private Account performAuthentication(final String email, final String password) throws AuthenticationException {
			Log.d(TAG, "performAuthentication()...");
			Account authenticatedUser = null;
			final AuthenticationRestClient client = new AuthenticationRestClient(email, password);
			authenticatedUser = client.authenticateUser();
			Log.d(TAG, "Account from client: " + authenticatedUser.toString());
			
//			HttpAuthentication authHeader = new HttpBasicAuthentication(user, password);
//
//			HttpHeaders requestHeaders = new HttpHeaders();
//			requestHeaders.setAuthorization(authHeader);
//			requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
//
//			// Create a new RestTemplate instance
//			RestTemplate restTemplate = new RestTemplate();
//			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
//			Log.d(TAG, "Trying to authenticate with " + user + ":" + password);
//			Account authenticatedUser = null;
//			try {
//				// Make the network request
//				ResponseEntity<Account> response = restTemplate.exchange("http://10.0.2.2:8080/todoapp-server/authenticate/",
//						HttpMethod.GET, requestEntity, Account.class);
//				if (response.getStatusCode() == HttpStatus.OK) {
//					authenticatedUser = response.getBody();
//					Log.d(TAG, "Response body: " + authenticatedUser);
//				}
//			}
//			catch (HttpClientErrorException e) {
//				Log.d(TAG, "Unable to authenticate");
//				throw new AuthenticationException("Unable to authenticate");
//			}
			return authenticatedUser;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * net.findme.android.ui.base.AbstractAsyncTask#onPostExecute(java.lang
		 * .Object)
		 */
		@Override
		protected void onPostExecute(final Account result) {
			super.onPostExecute(result);
			authenticationTask = null;
			if (result != null) { // Successfully authenticated
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
