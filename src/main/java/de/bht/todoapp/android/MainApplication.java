/**
 * 
 */
package de.bht.todoapp.android;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.provider.TodoItemDescriptor;

/**
 * @author markus
 * 
 */
public class MainApplication extends Application
{
	private static final String TAG = MainApplication.class.getSimpleName();
	private final static boolean CREATE_DATA = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		
		Log.d(TAG, "Application onCreate()...");
		if(CREATE_DATA) {
			storeTodoItems(5);
		}
		super.onCreate();
	}

	/*
	 * (non-Javadoc)
	 * 
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
		builder.setNeutralButton("Ok", new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		builder.show();
	}

	private void storeTodoItems(int size) {
		for (int i = 1; i <= size; i++) {
			final ContentValues value = new ContentValues();
			value.put(TodoItemDescriptor.SERVERID_COLUMN, i + TodoItemDescriptor.SERVERID_COLUMN);
			value.put(TodoItemDescriptor.TITLE_COLUMN, i + TodoItemDescriptor.TITLE_COLUMN);
			value.put(TodoItemDescriptor.DESCRIPTION_COLUMN, i + TodoItemDescriptor.DESCRIPTION_COLUMN);
			final String status = (i % 2 == 0) ? TodoItem.Status.OPEN.toString() : TodoItem.Status.CLOSED.toString();
			final boolean isFavourite = ((i % 2 == 0) ? Boolean.FALSE : Boolean.TRUE);
			value.put(TodoItemDescriptor.ISFAVOURITE_COLUMN, isFavourite);
			value.put(TodoItemDescriptor.STATUS_COLUMN, status);
			value.put(TodoItemDescriptor.DUEDATE_COLUMN, Calendar.getInstance().getTimeInMillis());
			value.put(TodoItemDescriptor.LATITUDE_COLUMN, 48.4532453 + 0.01);
			value.put(TodoItemDescriptor.LONGITUDE_COLUMN, 10.5324544 + 0.011);
			getContentResolver().insert(TodoItemDescriptor.CONTENT_URI, value);
		}

	}

}
