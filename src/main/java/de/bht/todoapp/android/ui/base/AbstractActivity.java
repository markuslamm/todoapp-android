/**
 * 
 */
package de.bht.todoapp.android.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import de.bht.todoapp.android.MainApplication;
import de.bht.todoapp.android.R;

/**
 * @author markus
 * 
 */
public abstract class AbstractActivity extends Activity implements BaseActivity
{
	protected static final String TAG = AbstractActivity.class.getSimpleName();

	private ProgressDialog progressDialog = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.ui.base.BaseActivity#getMainApplication()
	 */
	@Override
	public MainApplication getMainApplication() {
		return (MainApplication) getApplication();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.ui.base.BaseActivity#displayNetworkError()
	 */
	@Override
	public void displayNetworkError() {
		final String message = getString(R.string.error_remote);
		getMainApplication().displayToast(this, message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.ui.base.BaseActivity#displayAuthorizationError()
	 */
	@Override
	public void displayAuthorizationError() {
		final String message = getString(R.string.error_authorization);
		final String title = getString(R.string.error_title);
		getMainApplication().displayErrorAlert(this, title, message);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.todoapp.android.ui.base.BaseActivity#showProgressDialog(java.lang
	 * .String)
	 */
	@Override
	public void showProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.todoapp.android.ui.base.BaseActivity#dismissProgressDialog()
	 */
	@Override
	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()..");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()..");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()..");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()..");
	}

}
