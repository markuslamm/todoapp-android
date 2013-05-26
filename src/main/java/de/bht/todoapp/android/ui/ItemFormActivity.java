/**
 * Project: 	todoapp
 * Package:		de.bht.todoapp.ui.todo
 * Filename:	ItemFormActivity.java
 * Timestamp:	20.10.2012 | 16:08:35
 */
package de.bht.todoapp.android.ui;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.data.TodoItemDescriptor;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.ui.base.AbstractActivity;
import de.bht.todoapp.android.ui.base.AbstractAsyncTask;
import de.bht.todoapp.android.ui.base.BaseActivity;
import de.bht.todoapp.android.util.DateHelper;

/**
 * @author Markus Lamm
 * 
 */
public class ItemFormActivity extends AbstractActivity
{
	public static final String TAG = ItemFormActivity.class.getSimpleName();

	private TextView txtHeadline;
	private EditText txtTitle;
	private EditText txtDescription;
	private TextView txtDate;
	private TextView txtTime;

	private TextView txtLatitude;
	private TextView txtLongitude;

	private Spinner spnPriority;
	private Spinner spnStatus;
	
	private static final int DATE_DIALOG_ID = 10;
	private static final int TIME_DIALOG_ID = 20;

	private Uri itemUri = null;
	private TodoItem itemModel = null;

	private SaveItemTask saveTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_form);
		initWidgets();
		// new or edit item form
		final Bundle extras = getIntent().getExtras();
		String headline = null;

		// is update
		if (extras != null && extras.containsKey((TodoItemDescriptor.MIME_ITEM))) {
			headline = getString(R.string.label_update_item);
			// txtTitle.setHint("");
			// txtDescription.setHint("");
			itemUri = extras.getParcelable(TodoItemDescriptor.MIME_ITEM);
			Log.d(TAG, "Item URI received: " + itemUri);
		}
		// is new
		else {
			headline = getString(R.string.label_create_item);
			spnStatus.setEnabled(false);
			spnStatus.setSelection(0);
			spnPriority.setSelection(1);
		}
		txtHeadline.setText(headline);
		itemModel = populateItem(itemUri);
		if (itemModel != null) {
			fillData(itemModel);
		}
	}

	private TodoItem populateItem(final Uri uri) {
		final TodoItem item = new TodoItem();
		if (uri != null) {
			final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				final Long internalId = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.ID_COLUMN));
				final Long remoteId = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.SERVERID_COLUMN));
				final String title = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN));
				final String description = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.DESCRIPTION_COLUMN));
				final double latitude = cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LATITUDE_COLUMN));
				final double longitude = cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LONGITUDE_COLUMN));
				final long dueDate = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN));
				final String status = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.STATUS_COLUMN));
				final String priority = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.PRIORITY_COLUMN));
				cursor.close();

				item.setInternalId(internalId);
				item.setEntityId(remoteId);
				item.setTitle(title);
				item.setDescription(description);
				item.setLatitude(latitude);
				item.setLongitude(longitude);
				item.setDueDate(dueDate);
				item.setPriority(TodoItem.getPriorityFromString(priority));
				item.setStatus(TodoItem.getStatusFromString(status));
			}
			else {
				throw new RuntimeException("Cursor is NULL. Unable to populate model");
			}
		}
		else {
			item.setStatus(TodoItem.Status.OPEN);
			item.setPriority(TodoItem.Priority.MEDIUM);
			item.setDueDate(Calendar.getInstance(Locale.GERMANY).getTimeInMillis());
		}
		return item;
	}

	private void fillData(final TodoItem item) {
		if (item == null) {
			throw new RuntimeException("TodoItem is NULL. Unable to fill data");
		}
		txtTitle.setText(item.getTitle());
		txtDescription.setText(item.getDescription());
		txtLatitude.setText(String.valueOf(item.getLatitude()));
		txtLongitude.setText(String.valueOf(item.getLongitude()));
		txtDate.setText(DateHelper.getDateString(itemModel.getDueDate()));
		txtTime.setText(DateHelper.getTimeString(itemModel.getDueDate()));
		spnPriority.setSelection(TodoItem.getSpinnerPositionFromPriority(item.getPriority()));
		spnStatus.setSelection(item.getStatus().equals(TodoItem.Status.OPEN) ? 0 : 1);
	}

	private void initWidgets() {
		txtHeadline = (TextView) findViewById(R.id.headline);
		txtTitle = (EditText) findViewById(R.id.txt_item_title);
		txtDescription = (EditText) findViewById(R.id.txt_item_description);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtTime = (TextView) findViewById(R.id.txtTime);
		txtLatitude = (TextView) findViewById(R.id.txtLatitude);
		txtLongitude = (TextView) findViewById(R.id.txtLongitude);
		
		spnPriority = (Spinner) findViewById(R.id.spn_priority);
		spnPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				String item = parent.getAdapter().getItem(position).toString();
				Log.d(TAG, "Priority item selected: " + item);
				itemModel.setPriority(TodoItem.getPriorityFromString(item));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spnStatus = (Spinner) findViewById(R.id.spn_status);
		spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				String item = parent.getAdapter().getItem(position).toString();
				Log.d(TAG, "Status item selected: " + item);
				itemModel.setStatus(TodoItem.getStatusFromString(item));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void onClickSelectDate(final View view) {
		showDialog(DATE_DIALOG_ID);
	}

	public void onClickSelectTime(final View view) {
		showDialog(TIME_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance(Locale.GERMANY);
		if (itemUri != null && itemModel != null) {
			c.setTimeInMillis(itemModel.getDueDate());
		}
		switch (id) {
			case DATE_DIALOG_ID:
				Log.d(TAG, "Create DatePicker");
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				return new DatePickerDialog(this, datePickerListener, year, month, day);
			case TIME_DIALOG_ID:
				Log.d(TAG, "Create TimePicker");
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				return new TimePickerDialog(this, timePickerListener, hour, minute, true);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
			final Calendar c = Calendar.getInstance(Locale.GERMANY);
			c.setTimeInMillis(itemModel.getDueDate());
			c.set(Calendar.HOUR_OF_DAY, selectedHour);
			c.set(Calendar.MINUTE, selectedMinute);
			itemModel.setDueDate(c.getTimeInMillis());
			txtTime.setText(DateHelper.getTimeString(c.getTimeInMillis()));
		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			final Calendar c = Calendar.getInstance(Locale.GERMANY);
			c.setTimeInMillis(itemModel.getDueDate());
			c.set(Calendar.YEAR, selectedYear);
			c.set(Calendar.MONTH, selectedMonth);
			c.set(Calendar.DAY_OF_MONTH, selectedDay);
			itemModel.setDueDate(c.getTimeInMillis());
			txtDate.setText(DateHelper.getDateString(c.getTimeInMillis()));
		}
	};

	public void onClickSaveItem(final View view) {
		if (null == saveTask) {
			itemModel.setTitle(txtTitle.getText().toString());
			itemModel.setDescription(txtDescription.getText().toString());
			saveTask = new SaveItemTask(this, getString(R.string.progress_save_item));
			saveTask.execute(itemModel);
		}
	}

	private class SaveItemTask extends AbstractAsyncTask<TodoItem, Void, Uri>
	{
		// private Uri newUri = null;

		/**
		 * @param activity
		 * @param message
		 */
		public SaveItemTask(final BaseActivity activity, final String message)
		{
			super(activity, message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Uri doInBackground(TodoItem... items) {
			final TodoItem item = items[0];
			final ContentValues values = new ContentValues();
			values.put(TodoItemDescriptor.SERVERID_COLUMN, Math.round(Math.random() * 1000));
			values.put(TodoItemDescriptor.TITLE_COLUMN, item.getTitle());
			values.put(TodoItemDescriptor.DESCRIPTION_COLUMN, item.getDescription());
			values.put(TodoItemDescriptor.PRIORITY_COLUMN, item.getPriority().toString());
			values.put(TodoItemDescriptor.STATUS_COLUMN, item.getStatus().toString());
			values.put(TodoItemDescriptor.DUEDATE_COLUMN, item.getDueDate());
			values.put(TodoItemDescriptor.LATITUDE_COLUMN, 13.1111111);
			values.put(TodoItemDescriptor.LONGITUDE_COLUMN, 53.123456);
			Uri uri;
			if(itemUri == null) { // insert
				uri = getContentResolver().insert(TodoItemDescriptor.CONTENT_URI, values);
			}
			else {
				int count = getContentResolver().update(TodoItemDescriptor.CONTENT_URI, values, "_id=" + item.getInternalId(), null);
				uri = itemUri;
			}
			return uri;
		}

		@Override
		protected void onPostExecute(final Uri result) {
			super.onPostExecute(result);
			saveTask = null;
			if (result != null) {
				Log.d(TAG, "Item saved: " + result);
				final Intent intent = new Intent(ItemFormActivity.this, ItemListActivity.class);
				startActivity(intent);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

}
