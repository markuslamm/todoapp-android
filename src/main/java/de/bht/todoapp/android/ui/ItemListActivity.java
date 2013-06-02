/**
 * 
 */
package de.bht.todoapp.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.data.ItemService;
import de.bht.todoapp.android.data.db.LocalItemService;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
import de.bht.todoapp.android.data.rest.RestItemService;
import de.bht.todoapp.android.model.TodoItem;
import de.bht.todoapp.android.model.TodoItemList;
import de.bht.todoapp.android.ui.base.AbstractAsyncTask;
import de.bht.todoapp.android.ui.base.AbstractListActivity;
import de.bht.todoapp.android.ui.base.BaseActivity;

/**
 * @author markus
 * 
 */
public class ItemListActivity extends AbstractListActivity
{
	public static final String TAG = ItemListActivity.class.getSimpleName();
	private ItemListLoaderTask listLoaderTask = null;
	private TodoItemAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		if (null == listLoaderTask) {
			listLoaderTask = new ItemListLoaderTask(this, getString(R.string.progress_loadingitemlist));
			listLoaderTask.execute();
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final TodoItem item = listAdapter.getItem(position);
		Log.d(TAG, "Selected item: " + item);
		final Intent i = new Intent(this, ItemDetailActivity.class);
		i.putExtra(TodoItemDescriptor.MIME_ITEM, item);
		startActivity(i);
	}

	public void onClickCreateItem(final View view) {
		final Intent i = new Intent(this, ItemFormActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	private class ItemListLoaderTask extends AbstractAsyncTask<Void, Void, TodoItemList>
	{
		/**
		 * @param activity
		 * @param message
		 */
		public ItemListLoaderTask(final BaseActivity activity, final String message)
		{
			super(activity, message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected TodoItemList doInBackground(Void... params) {
			final ItemService restItemService = new RestItemService(ItemListActivity.this);
			final ItemService localItemService = new LocalItemService(getContentResolver());
			List<TodoItem> result = new ArrayList<TodoItem>();

			final TodoItemList localList = localItemService.findAllItems();
			/* check local db first */
			if (localList.getItems().size() > 0) {
				/* items in local db available */
				Log.d(TAG, "found items in local db: " + localList.getItems().size());
				if (getMainApplication().hasNetworkConnection()) {
					/*
					 * remote connection available, make rest call and insert
					 * local items remotely. rest service will persist items in
					 * local store via response handler
					 */
					final TodoItemList remoteList = restItemService.createItemList(localList);
					result = remoteList.getItems();
				}
			}
			else {
				/* local store empty */
				Log.d(TAG, "no items found in local db");
				if (getMainApplication().hasNetworkConnection()) {
					final TodoItemList remoteList = restItemService.findAllItems();
					result = remoteList.getItems();
				}
			}
			return new TodoItemList(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ItemListActivity.this.getListView().setEmptyView(ItemListActivity.this.findViewById(android.R.id.empty));
		}

		@Override
		protected void onPostExecute(TodoItemList itemList) {
			super.onPostExecute(itemList);
			listAdapter = new TodoItemAdapter(ItemListActivity.this, itemList.getItems());
			setListAdapter(listAdapter);
		}
	}
}
