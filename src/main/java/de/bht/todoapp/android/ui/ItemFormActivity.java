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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.provider.TodoItemDescriptor;
import de.bht.todoapp.android.ui.base.AbstractActivity;
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

	private CheckBox chkIsFavourite;
	private Spinner spnStatus;

	private int curreYear;
	private int currentMonth;
	private int currentDay;

	private Uri itemUri = null;
	private TodoItem itemModel = null;

	private static final String FORMAT_DATE = "dd-MM-yyyy | HH:mm";

	private static final int DATE_DIALOG_ID = 10;
	private static final int TIME_DIALOG_ID = 20;

	// private SharedPreferences settings;
	// private SaveItemTask saveTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_form);
		initWidgets();
		// settings = getMainApplication().getSettings();
		//

		// new or edit item form
		final Bundle extras = getIntent().getExtras();
		String headline = null;

		// is update
		if (extras != null && extras.containsKey((TodoItemDescriptor.MIME_ITEM))) {
			headline = getString(R.string.label_update_item);
			txtTitle.setHint("");
			txtDescription.setHint("");
			itemUri = extras.getParcelable(TodoItemDescriptor.MIME_ITEM);
			Log.d(TAG, "Item URI received: " + itemUri);
			itemModel = populateItem(itemUri);
			fillData(itemModel);
		}
		// is new
		else {
			headline = getString(R.string.label_create_item);
			spnStatus.setEnabled(false);
			itemModel = new TodoItem();
			itemModel.setStatus(TodoItem.Status.OPEN);
			itemModel.setDueDate(Calendar.getInstance(Locale.GERMANY).getTimeInMillis());
		}
		txtHeadline.setText(headline);
	}

	private TodoItem populateItem(final Uri uri) {
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		TodoItem item = null;
		if (cursor != null) {
			cursor.moveToFirst();
			final Long entityId = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.SERVERID_COLUMN));
			final String title = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN));
			final String description = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.DESCRIPTION_COLUMN));
			final double latitude = cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LATITUDE_COLUMN));
			final double longitude = cursor.getDouble(cursor.getColumnIndex(TodoItemDescriptor.LONGITUDE_COLUMN));
			final long dueDate = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN));
			final String status = cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.STATUS_COLUMN));
			final boolean isFavourite = (cursor.getInt(cursor.getColumnIndex(TodoItemDescriptor.ISFAVOURITE_COLUMN)) == 0) ? Boolean.FALSE
					: Boolean.TRUE;
			cursor.close();

			//create model
			item = new TodoItem();
			item.setEntityId(entityId);
			item.setTitle(title);
			item.setDescription(description);
			item.setLatitude(latitude);
			item.setLongitude(longitude);
			item.setDueDate(dueDate);
			item.setFavourite(isFavourite);
			item.setStatus(TodoItem.getStatusFromString(status));
		}
		return item;
	}
	

	private void fillData(final TodoItem item) {
		if (item != null) {
			// init values for display
			txtTitle.setText(item.getTitle());
			txtDescription.setText(item.getDescription());
			txtLatitude.setText(String.valueOf(item.getLatitude()));
			txtLongitude.setText(String.valueOf(item.getLongitude()));
			txtDate.setText(DateHelper.getDateString(itemModel.getDueDate()));
			txtTime.setText(DateHelper.getTimeString(itemModel.getDueDate()));
			boolean checked = (item.isFavourite()) ? Boolean.TRUE : Boolean.FALSE;
			chkIsFavourite.setChecked(checked);
			spnStatus.setSelection(item.getStatus().equals("OPEN") ? 0 : 1);
		}
	}

	private void initWidgets() {
		txtHeadline = (TextView) findViewById(R.id.headline);
		txtTitle = (EditText) findViewById(R.id.txt_item_title);
		txtDescription = (EditText) findViewById(R.id.txt_item_description);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtTime = (TextView) findViewById(R.id.txtTime);
		txtLatitude = (TextView) findViewById(R.id.txtLatitude);
		txtLongitude = (TextView) findViewById(R.id.txtLongitude);
		chkIsFavourite = (CheckBox) findViewById(R.id.chk_isFavourite);
		spnStatus = (Spinner) findViewById(R.id.spn_status);
		spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void onClickSaveItem(final View view) {
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
		if(itemUri != null && itemModel != null) {
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
			c.set(Calendar.HOUR_OF_DAY, selectedHour);
			c.set(Calendar.MINUTE, selectedMinute);
			itemModel.setDueDate(c.getTimeInMillis());
			fillData(itemModel);
		}
	};


	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			final Calendar c = Calendar.getInstance(Locale.GERMANY);
			c.set(Calendar.YEAR, selectedYear);
			c.set(Calendar.MONTH, selectedMonth);
			c.set(Calendar.DAY_OF_YEAR, selectedDay);
			itemModel.setDueDate(c.getTimeInMillis());
			fillData(itemModel);
		}
	};

}
