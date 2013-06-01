/**
 * 
 */
package de.bht.todoapp.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.data.ItemService;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
import de.bht.todoapp.android.data.rest.RestItemService;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.ui.base.AbstractActivity;
import de.bht.todoapp.android.ui.base.AbstractAsyncTask;
import de.bht.todoapp.android.ui.base.BaseActivity;
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
	private TextView txtPriority;

	private TodoItem itemModel;
	private DeleteItemTask deleteTask = null;

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
		txtPriority = (TextView) findViewById(R.id.txtPriority);

		final Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(TodoItemDescriptor.MIME_ITEM)) {
			itemModel = (TodoItem) extras.getSerializable(TodoItemDescriptor.MIME_ITEM);
			Log.d(TAG, "Item received: " + itemModel);
			fillData(itemModel);
		}
		else {
			throw new RuntimeException("item == NULL. Unable to display item details");
		}
	}

	private void fillData(final TodoItem itemModel) {
		txtTitle.setText(itemModel.getTitle());
		txtDescription.setText(itemModel.getDescription());
		txtLatitude.setText(String.valueOf(itemModel.getLatitude()));
		txtLongitude.setText(String.valueOf(itemModel.getLongitude()));
		txtDate.setText(DateHelper.getDateString(itemModel.getDueDate()));
		txtTime.setText(DateHelper.getTimeString(itemModel.getDueDate()));
		txtStatus.setText(itemModel.getStatus().toString());
		txtPriority.setText(itemModel.getPriority().toString());
	}

	public void onClickEditItem(final View view) {
		final Intent i = new Intent(this, ItemFormActivity.class);
		i.putExtra(TodoItemDescriptor.MIME_ITEM, itemModel);
		startActivity(i);
	}

	public void onClickDeleteItem(final View view) {
		if (null == deleteTask) {
			deleteTask = new DeleteItemTask(this, getString(R.string.progress_delete_item));
			deleteTask.execute(itemModel);
		}
	}

	private class DeleteItemTask extends AbstractAsyncTask<TodoItem, Void, Integer>
	{
		public DeleteItemTask(final BaseActivity activity, final String message)
		{
			super(activity, message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(TodoItem... items) {
			final TodoItem item = items[0];
			final ItemService itemService = new RestItemService(ItemDetailActivity.this);
			// final ItemService itemService = new
			// LocalItemService(getContentResolver());
			final Integer deleteCount = itemService.deleteItem(item);
			return deleteCount;
		}

		@Override
		protected void onPostExecute(final Integer result) {
			super.onPostExecute(result);
			deleteTask = null;
			if (result != null) {
				Log.d(TAG, "Item deleted. count: " + result);
				final Intent intent = new Intent(ItemDetailActivity.this, ItemListActivity.class);
				startActivity(intent);
			}
		}
	}

}
