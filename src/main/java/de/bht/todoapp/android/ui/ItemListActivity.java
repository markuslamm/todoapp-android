/**
 * 
 */
package de.bht.todoapp.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.bht.todoapp.android.R;
import de.bht.todoapp.android.data.IDataAccessor;
import de.bht.todoapp.android.data.db.TodoItemDescriptor;
import de.bht.todoapp.android.data.rest.ItemListHandler;
import de.bht.todoapp.android.data.rest.ResponseHandler;
import de.bht.todoapp.android.data.rest.RestClient;
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        if(null == listLoaderTask) {
        	listLoaderTask = new ItemListLoaderTask(this, getString(R.string.progress_loadingitemlist));
        	listLoaderTask.execute();
        }
    }
    
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final Intent i = new Intent(this, ItemDetailActivity.class);
		final Uri itemUri = Uri.parse(TodoItemDescriptor.CONTENT_URI + "/" + id);
		Log.d(TAG, String.format("Selected uri: %s", itemUri));
		i.putExtra(TodoItemDescriptor.MIME_ITEM, itemUri);
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

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected TodoItemList doInBackground(Void... params) {
			final String email = getMainApplication().getPreferences().getString("email", "");
			final String password = getMainApplication().getPreferences().getString("password", "");

			final IDataAccessor client = new RestClient(email, password);
			final TodoItemList itemList = client.findAllItems();
			return itemList;

			// return getContentResolver().query(TodoItemDescriptor.CONTENT_URI,
			// null, null, null, null);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//ItemListActivity.this.getListView().setEmptyView(ItemListActivity.this.findViewById(android.R.id.empty));
		}

		@Override
		protected void onPostExecute(TodoItemList itemList) {
			super.onPostExecute(itemList);
			final ResponseHandler<TodoItemList> handler = new ItemListHandler(activity);
			handler.handleResponse(itemList);
			final ItemListActivity activity = ItemListActivity.this;
			setListAdapter(new TodoItemAdapter(activity, itemList.getItems()));
		}
	}
}
