/**
 * 
 */
package de.bht.todoapp.android.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.provider.TodoItemDescriptor;
import de.bht.todoapp.android.ui.base.AbstractActivity;

/**
 * @author markus
 *
 */
public class ItemDetailActivity extends AbstractActivity
{
	public static final String TAG = ItemDetailActivity.class.getSimpleName();
	
	private TextView txtTitle;
	private TextView txtDescription;
	private TextView txtLatitude;
	private TextView txtLongitude;
	private TextView txtDueDate;
	private TextView txtStatus;
	private CheckBox chkIsFavourite;
	
	private Uri itemUri;
	
	private static final String FORMAT_DATE="dd-MM-yyyy | HH:mm";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtDueDate = (TextView) findViewById(R.id.txtDueDate);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        chkIsFavourite = (CheckBox) findViewById(R.id.chk_isFavourite);
        
        final Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(TodoItemDescriptor.MIME_ITEM)) {
			itemUri = extras.getParcelable(TodoItemDescriptor.MIME_ITEM);
			Log.d(TAG, "Item URI received: " + itemUri);
			fillData(itemUri);
		}
    }
    
    private void fillData(final Uri uri) {
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			txtTitle.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN)));
			txtDescription.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.DESCRIPTION_COLUMN)));
			txtLatitude.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.LATITUDE_COLUMN)));
			txtLongitude.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.LONGITUDE_COLUMN)));
			final Date dueDate = new Date(cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN)));
			
			//get formated date
			DateFormat f = new SimpleDateFormat(FORMAT_DATE);
			f.setTimeZone(TimeZone.getDefault());
			String date = f.format(dueDate);
			txtDueDate.setText(date);
			txtStatus.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.STATUS_COLUMN)));
			final int isFavourite = cursor.getInt(cursor.getColumnIndex(TodoItemDescriptor.ISFAVOURITE_COLUMN));
			chkIsFavourite.setChecked( (isFavourite == 0) ? Boolean.FALSE : Boolean.TRUE);
			cursor.close();
		}
    }
    
    public void onClickEditItem(final View view) {
    	final Intent i = new Intent(this, ItemFormActivity.class);
		i.putExtra(TodoItemDescriptor.MIME_ITEM, itemUri);
		startActivity(i);
    }
    
    

}
