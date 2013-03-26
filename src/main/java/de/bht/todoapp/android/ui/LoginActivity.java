/**
 * 
 */
package de.bht.todoapp.android.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.ui.base.AbstractActivity;

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
	
	
	/* (non-Javadoc)
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
	}
}
