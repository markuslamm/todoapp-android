/**
 * Project: 	todoapp
 * Package:		de.bht.todoapp.ui.todo
 * Filename:	ItemFormActivity.java
 * Timestamp:	20.10.2012 | 16:08:35
 */
package de.bht.todoapp.android.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
			//fillData(itemModel);
		}
		// is new
		else {
			headline = getString(R.string.label_create_item);
			spnStatus.setClickable(false);
			itemModel = new TodoItem();
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

			item = new TodoItem();
			item.setEntityId(entityId);
			item.setTitle(title);
			item.setDescription(description);
			item.setLatitude(latitude);
			item.setLongitude(longitude);
			//item.setDueDate(dueDate);
			item.setFavourite(isFavourite);
			item.setStatus(status);
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

			final String FORMAT_DATE = "dd-MM-yyyy";
			DateFormat f = new SimpleDateFormat(FORMAT_DATE);
			f.setTimeZone(TimeZone.getDefault());
			final String date = f.format(item.getDueDate());
			txtDate.setText(date);

			final String FORMAT_TIME = "HH:mm";
			f = new SimpleDateFormat(FORMAT_TIME);
			f.setTimeZone(TimeZone.getDefault());
			final String time = f.format(item.getDueDate());
			txtTime.setText(time);

			boolean checked = (item.isFavourite()) ? Boolean.TRUE : Boolean.FALSE;
			chkIsFavourite.setChecked(checked);

			if (item.getStatus().equals("OPEN")) {
				spnStatus.setSelection(0);
			}
			else {
				spnStatus.setSelection(1);
			}
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
		// if (null == saveTask) {
		// if (itemModel == null) {
		// itemModel = new TodoItem();
		// }
		// itemModel.setTitle(txtTitle.getText().toString());
		// itemModel.setDescription(txtDescription.getText().toString());
		// itemModel.setStatus(spnStatus.getSelectedItem().toString());
		// itemModel.setIsFavourite(chkIsFavourite.isChecked());
		// //itemModel.setDueDate(dueDate);
		// saveTask = new SaveItemTask(this,
		// getString(R.string.progress_savingitem));
		// saveTask.execute(itemModel);
		// }
	}

	// private Date

	// private class SaveItemTask extends AbstractAsyncTask<TodoItem, Void,
	// TodoItemVO>
	// {
	// private TodoItemVO transfer = null;
	//
	// /**
	// * @param activity
	// * @param message
	// */
	// public SaveItemTask(final BaseActivity activity, final String message)
	// {
	// super(activity, message);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see android.os.AsyncTask#doInBackground(Params[])
	// */
	// @Override
	// protected TodoItemVO doInBackground(TodoItem... items) {
	// // store remote
	// String user = settings.getString("user", "");
	// String password = settings.getString("password", "");
	// Long accountId = settings.getLong("accountId", 0L);
	//
	// HttpAuthentication authHeader = new HttpBasicAuthentication(user,
	// password);
	// HttpHeaders requestHeaders = new HttpHeaders();
	// requestHeaders.setAuthorization(authHeader);
	// requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	// requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	// // Create a new RestTemplate instance
	// RestTemplate restTemplate = new RestTemplate();
	// restTemplate.getMessageConverters().add(new
	// MappingJacksonHttpMessageConverter());
	//
	// final TodoItem item = items[0];
	// final HttpEntity<TodoItem> requestEntity = new HttpEntity<TodoItem>(item,
	// requestHeaders);
	// String url;
	// HttpMethod method;
	// // if is new item
	// if (itemUri == null) {
	// url = "http://10.0.2.2:8080/todoapp/items/" + accountId;
	// method = HttpMethod.POST;
	// }
	// else {
	// url = "http://10.0.2.2:8080/todoapp/items/" + accountId + "/" +
	// item.getEntityId();
	// method = HttpMethod.PUT;
	// }
	// ResponseEntity<TodoItemVO> response = restTemplate.exchange(url, method,
	// requestEntity, TodoItemVO.class);
	// if(response.getStatusCode() == HttpStatus.OK) {
	// transfer = response.getBody();
	// Log.d(TAG, "Response body: " + transfer);
	// }
	// return transfer;
	// }
	//
	// @Override
	// protected void onPostExecute(final TodoItemVO result) {
	// super.onPostExecute(result);
	// saveTask = null;
	// if(result != null) {
	// final ContentValues values = new ContentValues();
	// values.put(TodoItemDescriptor.ENTITYID_COLUMN, result.getEntityId());
	// values.put(TodoItemDescriptor.TITLE_COLUMN, result.getTitle());
	// values.put(TodoItemDescriptor.DESCRIPTION_COLUMN,
	// result.getDescription());
	// values.put(TodoItemDescriptor.STATUS_COLUMN, result.getStatus());
	// values.put(TodoItemDescriptor.ISFAVOURITE_COLUMN,
	// result.getIsFavourite());
	// values.put(TodoItemDescriptor.DUEDATE_COLUMN,
	// result.getDueDate().toString());
	// if (itemUri == null) {
	// final Uri insertUri =
	// getContentResolver().insert(TodoItemDescriptor.CONTENT_URI, values);
	// Log.d(TAG, "Insert Uri: " + insertUri);
	// }
	// else {
	// final int updateCount = getContentResolver().update(itemUri, values,
	// null, null);
	// Log.d(TAG, "Update count: " + updateCount);
	// }
	// final Intent intent = new Intent(ItemFormActivity.this,
	// ItemListActivity.class);
	// startActivity(intent);
	// }
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// }
	// }
	//

	//

	//

	//
	// /**
	// * @param str
	// * @return
	// */
	// private Status extractStatus(String str) {
	// Status status = null;
	// if (str.equals("DONE")) {
	// status = Status.DONE;
	// }
	// else if (str.equals("UNDONE")) {
	// status = Status.UNDONE;
	// }
	// else {
	// throw new IllegalArgumentException("Invalid status type: " + str);
	// }
	// return status;
	// }
	//
	public void onClickSelectDate(final View view) {
		showDialog(DATE_DIALOG_ID);
	}

	public void onClickSelectTime(final View view) {
		showDialog(TIME_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();
		switch (id) {
			case DATE_DIALOG_ID:
//				int year;
//				int month;
//				int day;
//				if (itemUri == null) {
//					year = c.get(Calendar.YEAR);
//					month = c.get(Calendar.MONTH);
//					day = c.get(Calendar.DAY_OF_MONTH);
//				}
//				else {
////					final Date date = new Date(itemModel.getDueDate());
////					year = date.getYear();
////					month = date.getMonth();
////					day = date.getDay();
//				}
//				// set date picker as current date
//				return new DatePickerDialog(this, datePickerListener, year, month, day);
//			case TIME_DIALOG_ID:
//				int hour;
//				int minute;
//				if (itemUri == null) {
//					hour = c.get(Calendar.HOUR);
//					minute = c.get(Calendar.MINUTE);
//				}
//				else {
////					final Date date = new Date(itemModel.getDueDate());
////					hour = date.getHours();
////					minute = date.getMinutes();
//				}
//				return new TimePickerDialog(this, timePickerListener, hour, minute, true);
//				
				
		}
		return null;
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
			if(itemModel.getDueDate() != null) {
				
			}
//			final Date date = new Date(selectedYear, selectedMonth, selectedDay);
//			itemModel.setDueDate(date.getTime());
//			final String strDate = DateHelper.getDateString(date);
//			txtDate.setText(strDate);
		}


	};


	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//			final Date date = new Date(selectedYear, selectedMonth, selectedDay);
//			itemModel.setDueDate(date.getTime());
//			final String strDate = DateHelper.getDateString(date);
//			txtDate.setText(strDate);
		}
	};

}
