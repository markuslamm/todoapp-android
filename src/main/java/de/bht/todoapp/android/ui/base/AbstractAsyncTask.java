/**
 * Project: 	todoapp
 * Package:		de.bht.todoapp.ui.base
 * Filename:	AbstractAsyncActivity.java
 * Timestamp:	20.10.2012 | 15:49:51
 */
package de.bht.todoapp.android.ui.base;

import android.os.AsyncTask;

/**
 * @author Markus Lamm
 *
 */
public abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    private static final String TAG = AbstractAsyncTask.class.getSimpleName();

    private final BaseActivity activity;
    private final String message;

    /**
     * @param activity
     * @param message
     */
    public AbstractAsyncTask(final BaseActivity activity, final String message)
    {
        this.activity = activity;
        this.message = message;
    }

    /**
     * @param context
     */
    public AbstractAsyncTask(final BaseActivity activity)
    {
        this(activity, "Loading. Please wait...");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        activity.showProgressDialog(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Result result) {
        activity.dismissProgressDialog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onCancelled()
     */
    @Override
    protected void onCancelled() {
        activity.dismissProgressDialog();
    }
}
