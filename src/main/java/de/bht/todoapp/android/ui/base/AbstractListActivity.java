/**
 * Project: 	todoapp
 * Package:		de.bht.todoapp.ui.base
 * Filename:	AbstractListActivity.java
 * Timestamp:	20.10.2012 | 15:51:35
 */
package de.bht.todoapp.android.ui.base;

import android.app.ListActivity;
import android.app.ProgressDialog;
import de.bht.todoapp.android.MainApplication;
import de.bht.todoapp.android.R;

/**
 * @author Markus Lamm
 *
 */
public abstract class AbstractListActivity extends ListActivity implements BaseActivity
{
    protected static final String TAG = AbstractActivity.class.getSimpleName();

    private ProgressDialog progressDialog = null;

    /*
     * (non-Javadoc)
     * 
     * @see net.twentyfourseven.todoapp.ui.base.BaseActivity#getAppContext()
     */
    public MainApplication getMainApplication() {
        return (MainApplication) getApplication();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.twentyfourseven.todoapp.ui.base.BaseActivity#displayNetworkError()
     */
    public void displayNetworkError() {
		final String message = getString(R.string.error_remote);
        getMainApplication().displayToast(this, message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.twentyfourseven.todoapp.ui.base.BaseActivity#displayAuthorizationError
     * ()
     */
    public void displayAuthorizationError() {
        final String message = getString(R.string.error_authorization);
        final String title = getString(R.string.error_title);
        getMainApplication().displayErrorAlert(this, title, message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.twentyfourseven.todoapp.ui.base.BaseActivity#showProgressDialog
     * (java.lang.String)
     */
    public void showProgressDialog(final String message) {
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
     * @see
     * net.twentyfourseven.todoapp.ui.base.BaseActivity#dismissProgressDialog
     * ()
     */
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
