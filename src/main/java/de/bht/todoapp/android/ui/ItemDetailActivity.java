/**
 * 
 */
package de.bht.todoapp.android.ui;

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
import de.bht.todoapp.android.util.DateHelper;

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
	private TextView txtDate;
	private TextView txtTime;

	private TextView txtStatus;
	private CheckBox chkIsFavourite;
	
	private Uri itemUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtDate = (TextView) findViewById(R.id.txtDueDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        chkIsFavourite = (CheckBox) findViewById(R.id.chk_isFavourite);
        chkIsFavourite.setEnabled(false);
        
        final Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(TodoItemDescriptor.MIME_ITEM)) {
			itemUri = extras.getParcelable(TodoItemDescriptor.MIME_ITEM);
			Log.d(TAG, "Item URI received: " + itemUri);
			fillData(itemUri);
		}
    }
    
    public void onClickDeleteItem(final View view) {
    	final int deleteCount = getContentResolver().delete(itemUri, null, null);
    	Log.d(TAG, deleteCount + " item deleted");
    	final Intent intent = new Intent(this, ItemListActivity.class);
    	startActivity(intent);
    }
    
    private void fillData(final Uri uri) {
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			txtTitle.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.TITLE_COLUMN)));
			txtDescription.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.DESCRIPTION_COLUMN)));
			txtLatitude.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.LATITUDE_COLUMN)));
			txtLongitude.setText(cursor.getString(cursor.getColumnIndex(TodoItemDescriptor.LONGITUDE_COLUMN)));
			final Long dateInMillis = cursor.getLong(cursor.getColumnIndex(TodoItemDescriptor.DUEDATE_COLUMN));
			txtDate.setText(DateHelper.getDateString(dateInMillis));
			txtTime.setText(DateHelper.getTimeString(dateInMillis));
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
