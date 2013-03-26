/**
 * 
 */
package de.bht.todoapp.android;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * @author markus
 * 
 */
public class MainApplication extends Application
{
	private static final String TAG = MainApplication.class.getSimpleName();

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, "Application onCreate()...");
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		Log.d(TAG, "Application onTerminate()...");
		super.onTerminate();
	}
	
	/**
     * @param context
     * @param message
     */
    public void displayErrorToast(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    
    public void displayErrorAlert(final Context context, final String title, final String description) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setNeutralButton("Ok", new Dialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
        builder.show();
    }

}
